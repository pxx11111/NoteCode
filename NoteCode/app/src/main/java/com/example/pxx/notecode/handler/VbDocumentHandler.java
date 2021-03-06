package com.example.pxx.notecode.handler;

import android.text.TextUtils;

public class VbDocumentHandler implements DocumentHandler {

	@Override
	public String getFileExtension() {
		return "ml";
	}

	@Override
	public String getFileFormattedString(String fileString) {
		return TextUtils.htmlEncode(fileString).replace("\n", "<br>");
	}

	@Override
	public String getFileMimeType() {
		return "text/html";
	}

	@Override
	public String getFilePrettifyClass() {
		return "prettyprint lang-vb";
	}

	@Override
	public String getFileScriptFiles() {
		return "<script src='file:///android_asset/lang-vb.js' type='text/javascript'></script> ";
	}

}
