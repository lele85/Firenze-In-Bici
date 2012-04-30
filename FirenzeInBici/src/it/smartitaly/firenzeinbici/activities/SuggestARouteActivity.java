package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SuggestARouteActivity extends Activity {
	
	EditText message_text, name_text;
	Button btnsend;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest_route);
		
		message_text = (EditText)findViewById(R.id.message_text);
		name_text = (EditText)findViewById(R.id.name_text);
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"apps4italy@e-xtrategy.net"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Nuova proposta di percorso");
		
		btnsend = (Button)findViewById(R.id.btnsend);
		btnsend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message_text.getText().toString() +
						"\n\n\n" + name_text.getText().toString());
				startActivity(Intent.createChooser(emailIntent, "Invia email con..."));
			}
		});
	}
	
}
