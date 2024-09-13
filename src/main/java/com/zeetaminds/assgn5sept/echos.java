package com.zeetaminds.assgn5sept;


import java.io.*;
import java.net.*;

class echoc {

    public static void main(String args[]) throws Exception
    {
        String sockin;
        try
        {
            Socket csock = new Socket("192.168.1.10",8881);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader br_sock = new BufferedReader(new InputStreamReader(csock.getInputStream()));
            PrintStream ps = new PrintStream(csock.getOutputStream());
            System.out.println("Start echoing... type 'end' to terminate");
            while((sockin=br.readLine())!=null)
            {
                ps.println(sockin);
                if(sockin.equals("end"))
                    break;
                else
                    System.out.println("echoed from server:"+br_sock.readLine());

            }
        }
        catch(UnknownHostException e)
        {
            System.out.println(e.toString());

        }
        catch(IOException ioe)
        {

            ioe.printStackTrace();
        }
    }
}
