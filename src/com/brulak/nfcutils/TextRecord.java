package com.brulak.nfcutils;
//pulled from http://developer.android.com/resources/samples/NFCDemo/src/com/example/android/nfc/record/TextRecord.html

import android.nfc.NdefRecord;
import android.util.Log;


import com.google.common.base.Preconditions;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * An NFC Text Record
 */
public class TextRecord implements ParsedNdefRecord {

	/** ISO/IANA language code */
	private final String mLanguageCode;

	private final String mText;

	private TextRecord(String languageCode, String text) {
		mLanguageCode = Preconditions.checkNotNull(languageCode);
		mText = Preconditions.checkNotNull(text);
	}

	

	public String getText() {
		return mText;
	}

	/**
	 * Returns the ISO/IANA language code associated with this text element.
	 */
	public String getLanguageCode() {
		return mLanguageCode;
	}

	public static TextRecord parseTextRecord(NdefRecord record) {
		byte[] payload = record.getPayload();
		try {
			String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
					: "UTF-16";
			int languageCodeLength = payload[0] & 0077;
			Log.d("reader", "Language length:" + languageCodeLength);
			String languageCode = new String(payload, 1, languageCodeLength,
					"US-ASCII");
			Log.d("reader", "Language code:" + languageCode);
			String text = new String(payload, languageCodeLength + 1,
					payload.length - languageCodeLength - 1, textEncoding);
			return new TextRecord(languageCode, text);
		} catch (UnsupportedEncodingException e) {
			// should never happen unless we get a malformed tag.
			throw new IllegalArgumentException(e);
		}
	}

	// TODO: deal with text fields which span multiple NdefRecords
	public static TextRecord parse(NdefRecord record) {
		byte[] payload = record.getPayload();
		Log.d("reader", "Payload Size:" + payload.length);
		Preconditions
				.checkArgument(record.getTnf() == NdefRecord.TNF_WELL_KNOWN
						|| record.getTnf() == NdefRecord.TNF_MIME_MEDIA);
		StringBuilder sb = new StringBuilder();

		byte[] type = record.getType();
		for (int k = 0; k < type.length; k++)
			sb.append("" + type[k]).append("\n");
		Log.d("reader", "Record Type:" + sb);
		Log.d("reader", "--------SIZE:" + payload.length + "------------------");
		sb = new StringBuilder();

		for (int k = 0; k < payload.length; k++)
			sb.append("" + payload[k]).append("\n");
		Log.d("reader", "Payload:\n" + sb);
		Log.d("reader", "--------------------------");

		Preconditions.checkArgument(Arrays.equals(record.getType(),
				NdefRecord.RTD_TEXT));
		try {

			/*
			 * payload[0] contains the "Status Byte Encodings" field, per the
			 * NFC Forum "Text Record Type Definition" section 3.2.1.
			 * 
			 * bit7 is the Text Encoding Field.
			 * 
			 * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1):
			 * The text is encoded in UTF16
			 * 
			 * Bit_6 is reserved for future use and must be set to zero.
			 * 
			 * Bits 5 to 0 are the length of the IANA language code.
			 */
			String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
					: "UTF-16";
			int languageCodeLength = payload[0] & 0077;
			Log.d("reader", "Language length:" + languageCodeLength);
			String languageCode = new String(payload, 1, languageCodeLength,
					"US-ASCII");
			Log.d("reader", "Language code:" + languageCode);
			String text = new String(payload, languageCodeLength + 1,
					payload.length - languageCodeLength - 1, textEncoding);
			return new TextRecord(languageCode, text);
		} catch (UnsupportedEncodingException e) {
			// should never happen unless we get a malformed tag.
			throw new IllegalArgumentException(e);
		}
	}

	public static boolean isText(NdefRecord record) {
		try {
			parse(record);
			return true;
		} catch (IllegalArgumentException e) {
			Log.d("reader", "Text Parsing error:" + e);
			return false;
		}
	}
}