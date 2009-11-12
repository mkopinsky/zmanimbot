package google.waveapps.zmanimbot;

import com.google.wave.api.ProfileServlet;

public class ZmanimBotProfile extends ProfileServlet {
  @Override
  public String getRobotName() {
    return "ZmanimBot";
  }
 
  @Override
  public String getRobotAvatarUrl() {
    return "http://a1.twimg.com/profile_images/282693586/zbot_bigger.jpg";
  }
 
  //@Override
  public String getRobotProfileUrl() {
    return "http://zmanimbot.blogspot.com/";
  }
}