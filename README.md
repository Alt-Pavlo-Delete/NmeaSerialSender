# NmeaSerialSender

This project was started as Java-educational project of QA engineer with further practical use. 

This small utility developed for QA department to emulate external GPS receivers.  
Utility reads NMEA0183 logs from the files and sends it to the desired COM-port with fixed frequency (*to work with COM ports used* https://github.com/Fazecast/jSerialComm.git *library*) its allows to use for NMEA0183 output various devices, such as usb-serial converters, built in bluetooth modems, etc.  
One of the main requirements was to make this utility easier to use for junior QA team members so GUI *(swing based)* was added, also utility allows to pause and continue transmission of the NMEA log and view the log of the sent NEMA messages in real time.

![image](https://user-images.githubusercontent.com/82888480/175921570-c02b83af-d1de-4951-a848-633c9ce74b0b.png)
