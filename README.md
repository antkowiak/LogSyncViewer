# LogSyncViewer
View multiple log files concurrently in one window, with log entries sorted by timestamp

This application allows you to view multiple log files in one window page, concurently.
The log entries in each file will be inter-weaved in chronological order.
The application assumes each line of each log file is prepended with a consistent
time/date stamp in the format: "MMM dd HH:mm:ss" as defined by the Java
SimpleDateFormat here:
https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
You can use a menu option to customize the expected date format.
