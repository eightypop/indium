package com.redwreckage.indium;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				String string = ((EditText) findViewById(R.id.editText1)).getText().toString();
				new SocketTask().execute(string);

				((EditText) findViewById(R.id.editText1)).setText("");
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public class SocketTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			String responseFromServer = getResponseFromServer(params[0]);

			return responseFromServer;
		}

		@Override
		protected void onPostExecute(String result) {
			TextView textView = (TextView) findViewById(R.id.textView1);
			textView.setText(result);
		}

		private String getResponseFromServer(String msg) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress("redwreckage.com", 50505), 500);
				if (socket.isConnected()) {
					OutputStream out = socket.getOutputStream();
					
					PrintWriter output = new PrintWriter(out);
					String text = msg;
					output.println(text);

					Scanner socketScanner = new Scanner(socket.getInputStream());
					String response = "";
					while (socketScanner.hasNext()) {
						response += socketScanner.nextLine();
					}

					out.flush();
					out.close();
					return response;
				} else {
					return "No one's home...";
				}

			} catch (UnknownHostException e) {
				return e.getMessage();
			} catch (IOException e) {
				return e.getMessage();
			}
		}

	}

}
