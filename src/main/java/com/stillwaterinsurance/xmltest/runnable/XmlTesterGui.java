package com.stillwaterinsurance.xmltest.runnable;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.stillwaterinsurance.xmltest.service.EngineService;
import com.stillwaterinsurance.xmltest.util.XmlTestUtils;

public class XmlTesterGui extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	JButton openButton;
	JTextArea sendLog;
	JTextArea receiveLog;
	JFileChooser fileChooser = new JFileChooser();

	public XmlTesterGui() {
		super(new BorderLayout());

		sendLog = new JTextArea(40, 80);
		sendLog.setMargin(new Insets(5, 5, 5, 5));
		sendLog.setEditable(false);
		final JScrollPane sendLogScrollPane = new JScrollPane(sendLog);

		receiveLog = new JTextArea(40, 80);
		receiveLog.setMargin(new Insets(5, 5, 5, 5));
		receiveLog.setEditable(false);
		final JScrollPane receiveLogScrollPane = new JScrollPane(receiveLog);

		openButton = new JButton("Choose File");
		openButton.addActionListener(this);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);

		final JPanel sendReceive = new JPanel();
		sendReceive.add(sendLogScrollPane, BorderLayout.WEST);
		sendReceive.add(receiveLogScrollPane, BorderLayout.EAST);

		add(buttonPanel, BorderLayout.NORTH);
		add(sendReceive, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {

		// Handle open button action.
		if (actionEvent.getSource() == openButton) {
			final int returnVal = fileChooser.showOpenDialog(XmlTesterGui.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final File file = fileChooser.getSelectedFile();
				System.out.println("Opening: " + file.getName() + ".");

				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(file));
					final StringBuilder sb = new StringBuilder();
					String line = "";
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}

					final String formattedRequest = XmlTestUtils.formatXML(sb.toString());
					System.out.println("Sending...\n");
					System.out.println(formattedRequest);
					sendLog.append(formattedRequest);

					final String responseXml = EngineService.callWSEngine(sb.toString());

					final String formattedResponse = XmlTestUtils.formatXML(responseXml);
					System.out.println("Receiving...\n");
					System.out.println(formattedResponse);
					receiveLog.append(formattedResponse);

				} catch (final FileNotFoundException e) {
					System.out.println("File not found: " + file);
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (final TransformerException e) {
					e.printStackTrace();
				} catch (final SOAPException e) {
					e.printStackTrace();
				} catch (final SAXException e) {
					e.printStackTrace();
				} catch (final ParserConfigurationException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				System.out.println("Open command cancelled by user.\n");
			}
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		final JFrame frame = new JFrame("XML tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new XmlTesterGui());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args) {
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

}
