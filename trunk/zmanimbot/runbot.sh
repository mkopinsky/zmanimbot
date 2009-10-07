export ZBOT_HOME=/home/mkopinsky/CodingProjects/ZmanimBot
cd $ZBOT_HOME

export LD_LIBRARY_PATH=$ZBOT_HOME/
CLASSPATH=$ZBOT_HOME/build/
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/accjwrap.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/zmanim-1.1.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/smack.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/smackx.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/jtwitter.jar

export CLASSPATH
echo Starting the bot.
/usr/bin/java zmanimbot.ZmanimBot >>log.txt 2>&1 & 

