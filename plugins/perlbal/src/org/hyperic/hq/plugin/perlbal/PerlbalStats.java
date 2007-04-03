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

package org.hyperic.hq.plugin.perlbal;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.hyperic.hq.plugin.netservices.NetServicesCollector;
import org.hyperic.hq.plugin.netservices.SocketWrapper;

public class PerlbalStats extends NetServicesCollector
{
    private static final String END = ".";
				private static final String REQUEST_KEY = "requests";
				private static final Pattern REQUEST_PATTERN = Pattern.compile("^reqs:");
				private SocketWrapper mySocket = null;

    public void collect()
				{
      try
						{
        startTime();
        mySocket = getSocketWrapper();
								setRequests();
      }
						catch (IOException e)
						{
        setAvailability(false);
        if (getMessage() == null)
          setErrorMessage(e.getMessage());
      }
						finally
						{
        if (mySocket != null) {
								  mySocket.close();
								  mySocket = null;
						  }
      }
    }

    private void setRequests()
    {
						try
      {
								String line;
								mySocket.writeLine("proc");
        while ((line = mySocket.readLine()) != null)
  						{
          if (line.startsWith(END))
            break;
          if (!REQUEST_PATTERN.matcher(line).find())
            continue;
          String [] array = line.split("\\s+");
          String value = array[1];
          setValue(REQUEST_KEY, value);
        }
        setAvailability(true);
        //XXX process stats
        endTime();
      }
						catch (IOException e)
						{
        setAvailability(false);
        if (getMessage() == null)
          setErrorMessage(e.getMessage());
      }
	   }
}
/*
												|nodes|
												127.0.0.1:60002 lastresponse 1175553970
												127.0.0.1:60002 requests 481
												127.0.0.1:60002 connects 1
												127.0.0.1:60002 lastconnect 1175553789
												127.0.0.1:60002 attempts 1
												127.0.0.1:60002 responsecodes 200 481
												127.0.0.1:60002 lastattempt 1175553789
												|proc|
												time: 1175553802
												pid: 2004
												reqs: 38 (+38)
*/
