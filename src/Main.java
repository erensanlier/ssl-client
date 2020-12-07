/**
 * Copyright [2017] [Yahya Hassanzadeh-Nazarabadi]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
public class Main
{
    public final static int KUSIS_ID = 60326;
    public final static int DD_MM_YY = 210498;
    public final static String TLS_SERVER_ADDRESS = "localhost";
    public final static int CERTIFICATE_PORT = 4444;
    public final static int TLS_SERVER_PORT = 1024 + ((KUSIS_ID + DD_MM_YY) % 65535);

    public static void main(String[] args) throws Exception
    {
        SLLCertifier certifier = new SLLCertifier(TLS_SERVER_ADDRESS, CERTIFICATE_PORT);
        certifier.connect();
        if(!certifier.getCertificate()){
            System.err.println("Couldn't get the certificate, terminating.");
            return;
        }

        SSLConnectToServer sslConnectToServer = new SSLConnectToServer(TLS_SERVER_ADDRESS, TLS_SERVER_PORT);
        sslConnectToServer.Connect();
        // Receive the KUSIS username and KUSIS ID here..
        StringBuilder message = new StringBuilder();
        int c;
        while((c = sslConnectToServer.getMessage()) != -1) {
            char character = (char) c;
            message.append(character);
        }
        System.out.println("Message received: " + message);
        sslConnectToServer.Disconnect();


    }
}
