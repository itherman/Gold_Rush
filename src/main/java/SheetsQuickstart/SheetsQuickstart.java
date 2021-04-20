package SheetsQuickstart;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import game.GameMenu;
import game.GoldRush;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SheetsQuickstart {
	
    private static final String APPLICATION_NAME = "leaderboard";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static void main(String[] args) throws IOException, GeneralSecurityException {
    	getSheetsData();
    	runGame();
    }
    
    
    private static void runGame() {
    	// Game Code: 
        GoldRush.player = JOptionPane.showInputDialog("Enter Name: ");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GoldRush.createAndShowGUI();
			}
		});
	}

    
    public static synchronized void updateData() throws IOException, GeneralSecurityException {
    	
    	ValueRange body1 = new ValueRange().setValues(Arrays.asList(
    	        Arrays.asList(GameMenu.data[0][1]),
    	        Arrays.asList(GameMenu.data[1][1]),
    	        Arrays.asList(GameMenu.data[2][1]),
    	        Arrays.asList(GameMenu.data[3][1]),
    	        Arrays.asList(GameMenu.data[4][1]),
    	        Arrays.asList(GameMenu.data[5][1]),
    	        Arrays.asList(GameMenu.data[6][1]),
    	        Arrays.asList(GameMenu.data[7][1]),
    	        Arrays.asList(GameMenu.data[8][1]),
    	        Arrays.asList(GameMenu.data[9][1])));
    	
    	ValueRange body2 = new ValueRange().setValues(Arrays.asList(
    	        Arrays.asList(GameMenu.data[0][0]),
    	        Arrays.asList(GameMenu.data[1][0]),
    	        Arrays.asList(GameMenu.data[2][0]),
    	        Arrays.asList(GameMenu.data[3][0]),
    	        Arrays.asList(GameMenu.data[4][0]),
    	        Arrays.asList(GameMenu.data[5][0]),
    	        Arrays.asList(GameMenu.data[6][0]),
    	        Arrays.asList(GameMenu.data[7][0]),
    	        Arrays.asList(GameMenu.data[8][0]),
    	        Arrays.asList(GameMenu.data[9][0])));
    	
        final String spreadsheetId = "1jJq7r_8E8BRBuWVUm4VhmouX7f_eAUUXJgkW2Qy7YHU";
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range1 = "B1";
        final String range2 = "A1";

    	Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    	UpdateValuesResponse col1result =
                service.spreadsheets().values().update(spreadsheetId,range1,body1).setValueInputOption("RAW").execute();
    	UpdateValuesResponse col2result =
                service.spreadsheets().values().update(spreadsheetId,range2,body2).setValueInputOption("RAW").execute();
    }
        
    
    /**
     * Prints the spreadsheet:
     */
	private static void getSheetsData() throws IOException, GeneralSecurityException {
    	
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1jJq7r_8E8BRBuWVUm4VhmouX7f_eAUUXJgkW2Qy7YHU";
        final String range = "A1:B10";
        
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
            JOptionPane.showMessageDialog(null, JOptionPane.ERROR_MESSAGE);
        } else {
        	int i = 0;
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s:%s\n", row.get(0), row.get(1));
                GameMenu.data[i][0] = (String) row.get(0);
                GameMenu.data[i][1] = (String) row.get(1);
                i++;
            }
        }
    }
    
    
}