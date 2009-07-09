package zmanimbot;

import com.aol.acc.*;
import java.util.*;


class AIMMessageListener extends ZmanimMessageListener implements AccEvents {
	ZmanimParser parser;
	
	AIMMessageListener() {
		super();
	}

	public void OnImReceived(AccSession session, AccImSession imSession, 
				AccParticipant participant, AccIm im) {
		try {
			String str = im.getConvertedText("text/plain");
			String chatter = participant.getName()+"@aim.com";
			if (str.startsWith("This message was sent from your") && str.contains("Wimzi widget"))
				;
			else if (chatter.equalsIgnoreCase("AOL System Msg@aim.com"))
				;
			else
				imSession.sendIm(session.createIm(parse(str,chatter), null));
//				parse(str,chatter);
		} catch (Exception ex) {
			System.out.println(new Date() + "\tCaught in AIMMessageListener.OnImReceived");
			ex.printStackTrace();
		}
					
	}
	
	
	
	
	
	
	
	/* These are the methods that I'm required to have to implement AccEvents, 
	 *		but the only one I'll use is OnImReceived (above).
	 */
	public void OnStateChange(AccSession parm1, AccSessionState parm2, AccResult parm3) {
	}
	public void OnSessionChange(AccSession parm1, AccSessionProp parm2) {
	}
	public void OnIdleStateChange(AccSession parm1, int parm2) {
	}
	public void OnInstanceChange(AccSession parm1, AccInstance parm2, 
				AccInstance parm3, AccInstanceProp parm4) {
	}
	public void OnLookupUsersResult(AccSession parm1, String[] parm2, int parm3, 
				AccResult parm4, AccUser[] parm5) {
	}
	public void OnSearchDirectoryResult(AccSession parm1, int parm2, 
				AccResult parm3, AccDirEntry parm4) {
	}
	public void OnSendInviteMailResult(AccSession parm1, int parm2, 
				AccResult parm3) {
	}
	public void OnRequestServiceResult(AccSession parm1, int parm2, 
				AccResult parm3, String parm4, int parm5, byte[] parm6) {
	}
	public void OnConfirmAccountResult(AccSession parm1, int parm2, 
				AccResult parm3) {
	}
	public void OnReportUserResult(AccSession parm1, AccUser parm2, int parm3, 
				AccResult parm4, int parm5, int parm6) {
	}
	public void OnAlertReceived(AccSession parm1, AccAlert parm2) {
	}
	public void OnPreferenceResult(AccSession parm1, String parm2, int parm3, 
				String parm4, AccResult parm5) {
	}
	public void OnPreferenceChange(AccSession parm1, String parm2, 
				AccResult parm3) {
	}
	public void OnPreferenceInvalid(AccSession parm1, String parm2, 
				AccResult parm3) {
	}
	public void OnPluginChange(AccSession parm1, AccPluginInfo parm2, 
				AccPluginInfoProp parm3) {
	}
	public void OnBartItemRequestPropertyResult(AccSession parm1, 
				AccBartItem parm2, AccBartItemProp parm3, int parm4, AccResult parm5, 
				AccVariant parm6) {
	}
	public void OnUserRequestPropertyResult(AccSession parm1, AccUser parm2, 
				AccUserProp parm3, int parm4, AccResult parm5, AccVariant parm6) {
	}
	public void OnGroupAdded(AccSession parm1, AccGroup parm2, int parm3, 
				AccResult parm4) {
	}
	public void OnGroupRemoved(AccSession parm1, AccGroup parm2, 
				AccResult parm3) {
	}
	public void OnGroupMoved(AccSession parm1, AccGroup parm2, int parm3, 
				int parm4, AccResult parm5) {
	}
	public void OnBuddyAdded(AccSession parm1, AccGroup parm2, AccUser parm3, 
				int parm4, AccResult parm5) {
	}
	public void OnBuddyRemoved(AccSession parm1, AccGroup parm2, AccUser parm3, 
				AccResult parm4) {
	}
	public void OnBuddyMoved(AccSession parm1, AccUser parm2, AccGroup parm3, 
				int parm4, AccGroup parm5, int parm6, AccResult parm7) {
	}
	public void OnBuddyListChange(AccSession parm1, AccBuddyList parm2, 
				AccBuddyListProp parm3) {
	}
	public void OnGroupChange(AccSession parm1, AccGroup parm2, 
				AccGroupProp parm3) {
	}
	public void OnUserChange(AccSession parm1, AccUser parm2, AccUser parm3, 
				AccUserProp parm4, AccResult parm5) {
	}
	public void OnChangesBegin(AccSession parm1) {
	}
	public void OnChangesEnd(AccSession parm1) {
	}
	public void OnNewSecondarySession(AccSession parm1, 
				AccSecondarySession parm2, int parm3) {
	}
	public void OnSecondarySessionStateChange(AccSession parm1, 
				AccSecondarySession parm2, AccSecondarySessionState parm3, AccResult parm4) {
	}
	public void OnSecondarySessionChange(AccSession parm1, 
				AccSecondarySession parm2, int parm3) {
	}
	public void OnParticipantJoined(AccSession parm1, AccSecondarySession parm2, 
				AccParticipant parm3) {
	}
	public void OnParticipantChange(AccSession parm1, AccSecondarySession parm2, 
				AccParticipant parm3, AccParticipant parm4, AccParticipantProp parm5) {
	}
	public void OnParticipantLeft(AccSession parm1, AccSecondarySession parm2, 
				AccParticipant parm3, AccResult parm4, String parm5, String parm6) {
	}
	public void OnInviteResult(AccSession parm1, AccSecondarySession parm2, 
				String parm3, int parm4, AccResult parm5) {
	}
	public void OnEjectResult(AccSession parm1, AccSecondarySession parm2, 
				String parm3, int parm4, AccResult parm5) {
	}
	public void BeforeImSend(AccSession parm1, AccImSession parm2, 
				AccParticipant parm3, AccIm parm4) {
	}
	public void OnImSent(AccSession parm1, AccImSession parm2, 
				AccParticipant parm3, AccIm parm4) {
	}
	public void OnImSendResult(AccSession parm1, AccImSession parm2, 
				AccParticipant parm3, AccIm parm4, AccResult parm5) {
	}
	public void BeforeImReceived(AccSession parm1, AccImSession parm2, 
				AccParticipant parm3, AccIm parm4) {
	}
	public void OnLocalImReceived(AccSession parm1, AccImSession parm2, 
				AccIm parm3) {
	}
	public void OnInputStateChange(AccSession parm1, AccImSession parm2, 
				String parm3, AccImInputState parm4) {
	}
	public void OnEmbedDownloadProgress(AccSession parm1, AccImSession parm2, 
				AccIm parm3, String parm4, AccStream parm5) {
	}
	public void OnEmbedDownloadComplete(AccSession parm1, AccImSession parm2, 
				AccIm parm3) {
	}
	public void OnEmbedUploadProgress(AccSession parm1, AccImSession parm2, 
				AccIm parm3, String parm4, AccStream parm5) {
	}
	public void OnEmbedUploadComplete(AccSession parm1, AccImSession parm2, 
				AccIm parm3) {
	}
	public void OnRateLimitStateChange(AccSession parm1, AccImSession parm2, 
				AccRateState parm3) {
	}
	public void OnNewFileXfer(AccSession parm1, AccFileXferSession parm2, 
				AccFileXfer parm3) {
	}
	public void OnFileXferProgress(AccSession parm1, AccFileXferSession parm2, 
				AccFileXfer parm3) {
	}
	public void OnFileXferCollision(AccSession parm1, AccFileXferSession parm2, 
				AccFileXfer parm3) {
	}
	public void OnFileXferComplete(AccSession parm1, AccFileXferSession parm2, 
				AccFileXfer parm3, AccResult parm4) {
	}
	public void OnFileXferSessionComplete(AccSession parm1, 
				AccFileXferSession parm2, AccResult parm3) {
	}
	public void OnFileSharingRequestListingResult(AccSession parm1, 
				AccFileSharingSession parm2, AccFileSharingItem parm3, int parm4, 
				AccResult parm5) {
	}
	public void OnFileSharingRequestXferResult(AccSession parm1, 
				AccFileSharingSession parm2, AccFileXferSession parm3, int parm4, 
				AccFileXfer parm5) {
	}
	public void OnAvStreamStateChange(AccSession parm1, AccAvSession parm2, 
				String parm3, AccAvStreamType parm4, AccSecondarySessionState parm5, 
				AccResult parm6) {
	}
	public void OnAvManagerChange(AccSession parm1, AccAvManager parm2, 
				AccAvManagerProp parm3, AccResult parm4) {
	}
	public void OnAudioLevelChange(AccSession parm1, AccAvSession parm2, 
				String parm3, int parm4) {
	}
	public void OnSoundEffectReceived(AccSession parm1, AccAvSession parm2, 
				String parm3, String parm4) {
	}
	public void OnCustomSendResult(AccSession parm1, AccCustomSession parm2, 
				AccParticipant parm3, AccIm parm4, AccResult parm5) {
	}
	public void OnCustomDataReceived(AccSession parm1, AccCustomSession parm2, 
				AccParticipant parm3, AccIm parm4) {
	}
	public void OnRequestSummariesResult(AccSession parm1, int parm2, 
				AccResult parm3, AccVariant parm4) {
	}

	public void OnDeliverStoredImsResult(AccSession parm1, int parm2, 
				AccResult parm3) {
	}
	public void OnDeleteStoredImsResult(AccSession parm1, int parm2, 
				AccResult parm3) {
	}	
}
