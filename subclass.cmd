setlocal

call ant -f subclass.xml -Dsearch.dir=%1 clean jar

endlocal
