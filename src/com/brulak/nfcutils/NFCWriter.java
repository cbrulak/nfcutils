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

import java.util.Locale;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

public class NFCWriter {

	public Boolean isTagPresent(Intent intent)
	{
		Tag t = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		return t != null;
	}
	
	public Boolean doesTagNeedFormatting(Intent intent)
	{
		String action = intent.getAction();
		return NfcAdapter.ACTION_TAG_DISCOVERED.equals(action);
	}
	
	public Boolean formatTag(Intent intent,String defaultMessage)
	{
    	if (doesTagNeedFormatting(intent) && isTagPresent(intent))
    	{
    		//ClientUtils.showToast(this, "Formating...");
    		Tag t = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    		NdefFormatable tag = NdefFormatable.get(t);
    		Locale locale = Locale.US;
        	final byte[] langBytes = locale.getLanguage().getBytes(Charsets.US_ASCII);
        	String text = defaultMessage;
        	final byte[] textBytes = text.getBytes(Charsets.UTF_8);
        	final int utfBit = 0;
        	final char status = (char) (utfBit + langBytes.length);
        	final byte[] data = Bytes.concat(new byte[] {(byte) status}, langBytes, textBytes);
        	NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
        	try {
        	    NdefRecord[] records = {record};
        	    NdefMessage message = new NdefMessage(records);
        	    tag.connect();
        	    tag.format(message);
        	    return true;
        	}
        	catch (Exception e){
        		//ClientUtils.showToast(this, "Exception " + e.toString());
        		//Log.d(TAG, "LeaveMessageException:" + e.toString());
        		//Log.d(TAG, "LeaveMessageException:" + e.getCause());
        		//Log.d(TAG, "LeaveMessageException:" + e.getStackTrace());
        	}
    	}
    	return false;
	}
	
	public Boolean writeMessage(Intent intent,String message)
	{

    	Tag t = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    	Ndef tag = Ndef.get(t);
    	
//    	if(t == null) 
//	    {
//    		ClientUtils.showToast(this, "Tag t is NULL");
//    		Log.d(TAG, "Tag t is NULL");
//	    }
//    	else
//    	{
//    		String[] list =  t.getTechList();
//    		for(int i = 0; i < list.length; i++)
//    		{
//    			Log.d(TAG, "Tag tech list is " +list[i].toString());
//    		}
//    		Log.d(TAG, "Tag toString is " + t.toString());
//    	}
//    	
//    	
//	    if(tag == null) 
//	    {
//	    	ClientUtils.showToast(this, "intent is " + intent.toString());
//    		ClientUtils.showToast(this, "NdefFormatable tag is NULL");
//    		Log.d(TAG, "NdefFormatable tag is NULL");
//	    }
    	
    	Locale locale = Locale.US;
    	final byte[] langBytes = locale.getLanguage().getBytes(Charsets.US_ASCII);
    	String text = message;
    	final byte[] textBytes = text.getBytes(Charsets.UTF_8);
    	final int utfBit = 0;
    	final char status = (char) (utfBit + langBytes.length);
    	final byte[] data = Bytes.concat(new byte[] {(byte) status}, langBytes, textBytes);
    	NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    	try {
    	    NdefRecord[] records = {record};
    	    NdefMessage ndfmessage = new NdefMessage(records);
    	    tag.connect();
    	    tag.writeNdefMessage(ndfmessage);
    	    return true;
    	}
    	catch (Exception e){
//    		ClientUtils.showToast(this, "Exception " + e.toString());
//    		Log.d(TAG, "LeaveMessageException:" + e.toString());
//    		Log.d(TAG, "LeaveMessageException:" + e.getCause());
//    		Log.d(TAG, "LeaveMessageException:" + e.getStackTrace());
    	}
    	return false;
	
	}
}
