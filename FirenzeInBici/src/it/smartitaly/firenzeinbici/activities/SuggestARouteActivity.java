package it.smartitaly.firenzeinbici.activities;

import it.smartitaly.firenzeinbici.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
		

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.disclaimer_percorsi).setPositiveButton("Ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@smartitaly.it"});
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.getParent().onBackPressed();
	}
	
}
