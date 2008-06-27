/*
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 * 
 * Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
 * This file is part of HQ.
 * 
 * HQ is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package org.hyperic.hq.agent.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.hyperic.hq.agent.AgentCommandsAPI;
import org.hyperic.hq.agent.AgentConnectionException;
import org.hyperic.hq.agent.AgentRemoteException;
import org.hyperic.hq.agent.AgentRemoteValue;
import org.hyperic.hq.agent.AgentStreamPair;
import org.hyperic.hq.agent.FileData;
import org.hyperic.hq.agent.FileDataResult;
import org.hyperic.hq.agent.commands.AgentBundle_args;
import org.hyperic.hq.agent.commands.AgentBundle_result;
import org.hyperic.hq.agent.commands.AgentDie_args;
import org.hyperic.hq.agent.commands.AgentDie_result;
import org.hyperic.hq.agent.commands.AgentPing_args;
import org.hyperic.hq.agent.commands.AgentPing_result;
import org.hyperic.hq.agent.commands.AgentReceiveFileData_args;
import org.hyperic.hq.agent.commands.AgentRestart_args;
import org.hyperic.hq.agent.commands.AgentRestart_result;
import org.hyperic.hq.agent.commands.AgentUpgrade_args;
import org.hyperic.hq.agent.commands.AgentUpgrade_result;
import org.hyperic.util.math.MathUtil;

/**
 * The set of commands a client can call to a remote agent.  This object
 * provides a specific API which wraps the generic functions which 
 * a remote agent implements.
 */

public class LegacyAgentCommandsClientImpl implements AgentCommandsClient {
    private AgentConnection  agentConn;   
    private AgentCommandsAPI verAPI;

    /**
     * Create the object which communicates over a passed connection.
     *
     * @args agentConn the connection to use when making requests to the agent.
     */
    public LegacyAgentCommandsClientImpl(AgentConnection agentConn){
        this.agentConn = agentConn;
        this.verAPI    = new AgentCommandsAPI();
    }

    /**
     * @see org.hyperic.hq.agent.client.AgentCommandsClient#ping()
     */
    public long ping()
        throws AgentRemoteException, AgentConnectionException 
    {
        AgentPing_args args = new AgentPing_args();
        AgentPing_result result;
        AgentRemoteValue cmdRes;
        long sendTime, recvTime;

        sendTime = System.currentTimeMillis();
        cmdRes = this.agentConn.sendCommand(AgentCommandsAPI.command_ping,
                                            this.verAPI.getVersion(), args);
        recvTime = System.currentTimeMillis();

        // We don't really do anything with this result object -- it's just
        // here for future expansion.
        result = new AgentPing_result(cmdRes);
        return recvTime - sendTime;
    }

    /**
     * @see org.hyperic.hq.agent.client.AgentCommandsClient#restart()
     */
    public void restart()
        throws AgentRemoteException, AgentConnectionException 
    {
        AgentRestart_args args = new AgentRestart_args();
        AgentRestart_result result;
        AgentRemoteValue cmdRes;

        cmdRes = this.agentConn.sendCommand(AgentCommandsAPI.command_restart,
                this.verAPI.getVersion(), args);

        // We don't really do anything with this result object -- it's just
        // here for future expansion.
        result = new AgentRestart_result(cmdRes);
    }    

    /**
     * @see org.hyperic.hq.agent.client.AgentCommandsClient#die()
     */
    public void die()
        throws AgentRemoteException, AgentConnectionException 
    {
        AgentDie_args args = new AgentDie_args();
        AgentDie_result result;
        AgentRemoteValue cmdRes;

        cmdRes = this.agentConn.sendCommand(AgentCommandsAPI.command_die,
                                            this.verAPI.getVersion(), args);

        result = new AgentDie_result(cmdRes);
    }

    private FileDataResult[] sendData(OutputStream outStream,
                                      FileData[] destFiles,
                                      InputStream[] streams)
        throws IOException, AgentRemoteException
    {
        FileDataResult[] res = new FileDataResult[destFiles.length];
        byte[] sendBuf = new byte[8192];

        for(int i=0; i<destFiles.length; i++){
            long startTime = System.currentTimeMillis();
            long toSend = destFiles[i].getSize();
            
            while(toSend > 0){
                int nToRead, nBytes;

                if((nToRead = streams[i].available()) == 0){
                    throw new AgentRemoteException("No available bytes to " +
                                                   "read for '" + 
                                                   destFiles[i].getDestFile() +
                                                   "' - needed " + toSend +
                                                   " more bytes to complete");
                }
                
                nToRead = MathUtil.clamp(nToRead, 1, sendBuf.length);
                nBytes  = streams[i].read(sendBuf, 0, nToRead);
                if(nBytes == -1){
                    throw new AgentRemoteException("Error reading from for '" +
                                                   destFiles[i].getDestFile() +
                                                   "' - read returned -1");
                }

                outStream.write(sendBuf, 0, nBytes);
                toSend -= nBytes;
            }

            long sendTime = System.currentTimeMillis() - startTime;
            res[i] = new FileDataResult(destFiles[i].getDestFile(),
                                        destFiles[i].getSize(), sendTime);
        }

        return res;
    }

    /**
     * @see org.hyperic.hq.agent.client.AgentCommandsClient#agentSendFileData(org.hyperic.hq.agent.FileData[], java.io.InputStream[])
     */
    public FileDataResult[] agentSendFileData(FileData[] destFiles,
                                              InputStream[] streams)
        throws AgentRemoteException, AgentConnectionException
    {
        final String cmd = this.verAPI.command_receive_file;
        AgentReceiveFileData_args args;
        AgentStreamPair sPair;

        if(destFiles.length != streams.length){
            throw new IllegalArgumentException("Streams and dest files " +
                                               "arrays must be the same " +
                                               "length");
        }

        args = new AgentReceiveFileData_args();

        for(int i=0; i < destFiles.length; i++){
            args.addFileData(destFiles[i]);
        }
        
        sPair = this.agentConn.sendCommandHeaders(cmd, 
                                                  this.verAPI.getVersion(), 
                                                  args);
        try {            
            FileDataResult[] rs = this.sendData(sPair.getOutputStream(), destFiles, streams);
            
            // this is necessary so that remote exceptions are propagated 
            // back to the client
            this.agentConn.getCommandResult(sPair);
            
            return rs;
        } catch(IOException exc){
            throw new AgentRemoteException("IO Exception while sending " +
                                           "file data: " + exc.getMessage());
        } finally {
            // make sure the socket is closed - may not be closed if sendData() 
            // throws an exception
           try {
               sPair.close();
           } catch (IOException e) {
            // swallow
           } 
        }
    }

    /**
     * @see org.hyperic.hq.agent.client.AgentCommandsClient#upgrade(java.lang.String, java.lang.String)
     */
    public void upgrade(String tarFile, String destination)
            throws AgentRemoteException, AgentConnectionException {
        // set the arguments to the command
        AgentUpgrade_args args = new AgentUpgrade_args(tarFile, destination);
        AgentUpgrade_result result;
        AgentRemoteValue cmdRes;

        cmdRes = this.agentConn.sendCommand(AgentCommandsAPI.command_upgrade,
                this.verAPI.getVersion(), args);
        
        // We don't really do anything with this result object -- it's just
        // here for future expansion.
        result = new AgentUpgrade_result(cmdRes);
    }
    
    /**
     * @see org.hyperic.hq.agent.client.AgentCommandsClient#getCurrentAgentBundle()
     */
    public String getCurrentAgentBundle() throws AgentRemoteException, AgentConnectionException {
        
        AgentBundle_args args = new AgentBundle_args();
        
        AgentRemoteValue cmdRes = 
            this.agentConn.sendCommand(AgentCommandsAPI.command_getCurrentAgentBundle, 
                                       this.verAPI.getVersion(), 
                                       args);
        
        return new AgentBundle_result(cmdRes).getCurrentAgentBundle();
    }    

}
