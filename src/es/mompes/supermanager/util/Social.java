package es.mompes.supermanager.util;


import android.app.Activity;
import android.content.Intent;

public class Social extends Activity{
	public void compartir(final String subject, final String text) {

		final Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");

		intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		intent.putExtra(Intent.EXTRA_TEXT, text);

		startActivity(Intent.createChooser(intent, "Share"));
	}
}
