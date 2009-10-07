export ZBOT_HOME=/home/mkopinsky/CodingProjects/ZmanimBot
cd "$ZBOT_HOME"

export LD_LIBRARY_PATH=$ZBOT_HOME/
CLASSPATH=$ZBOT_HOME/build/
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/accjwrap.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/zmanim-1.1.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/smack.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/smackx.jar
CLASSPATH=$CLASSPATH:$ZBOT_HOME/lib/jtwitter.jar
export CLASSPATH

if [ ! -d $ZBOT_HOME/build ]; then
   echo Creating $ZBOT_HOME/build directory
   mkdir $ZBOT_HOME/build
fi

echo Compiling ZmanimBot....
javac -cp "$CLASSPATH" -d "$ZBOT_HOME/build/" $ZBOT_HOME/src/zmanimbot/*.java
echo Done. 

