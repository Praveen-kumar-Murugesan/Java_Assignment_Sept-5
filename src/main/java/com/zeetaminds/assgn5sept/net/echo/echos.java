package com.zeetaminds.assgn5sept.net.echo;


import java.io.*;
import java.net.*;
class echos {
    public static void main(String args[]) throws Exception
    {
        String echoin;
        ServerSocket svrsoc;
        Socket soc;
        BufferedReader br;

        try {
            svrsoc = new ServerSocket(8881);
            soc = svrsoc.accept();
            br = new BufferedReader (new InputStreamReader(soc.getInputStream()));
            PrintStream ps = new PrintStream(soc.getOutputStream());
            System.out.println("Connected for echo:");
            while((echoin=br.readLine())!=null)
            {
                if(echoin.equals("end"))
                {
                    System.out.println("Client disconnected");
                    br.close();
                    break;
                }
                else
                    ps.println(echoin);
            }
        } catch(IOException e) {
            System.out.println(e.toString());
        }

    }
}
