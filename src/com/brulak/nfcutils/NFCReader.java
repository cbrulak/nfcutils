/*

Copyright (c) 2011, Chris Brulak
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.brulak.nfcutils;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class NFCReader {
	

	static final String COUPON_MIME_TYPE = "text/plain";
	
	public String ReadMessageFromTag(Intent intent)
	{
		String messageToRtn = ""; 
		///Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		if(rawMsgs != null && rawMsgs.length > 0)
		{
			NdefMessage msg = (NdefMessage) rawMsgs[0];
			Message message = buildMessage(msg);
			//ClientUtils.showToast(this, "Message " + message.getDesc());
			messageToRtn = message.getDesc();
	//		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
	  //      NdefMessage msg2 = formMessage();
	   //     adapter.enableForegroundNdefPush(this, msg2);
		}
		return messageToRtn;
	}
    
    private Message buildMessage(NdefMessage msg) {

		NdefRecord record = msg.getRecords()[0];
		TextRecord tr = TextRecord.parseTextRecord(record);
		//Gson gson = new Gson();
		Message message = new Message(); 
		message.setDesc(tr.getText());
		
		//gson.fromJson(tr.getText(), Message.class);
		
		return message;
	}
    
    static public class Message {
        String id;
        String desc;
        String cname;

		public boolean validate() {
			// TODO Auto-generated method stub
			return true;
		}
        public String getId() {
             return id;
        }
        public void setId(String id) {
             this.id = id;
        }
        public String getDesc() {
             return desc;
        }
        public void setDesc(String descr) {
             this.desc = descr;
        }
        public String getCname() {
             return cname;
        }
        public void setCname(String cname) {
             this.cname = cname;
        }
       
    
   }
}
