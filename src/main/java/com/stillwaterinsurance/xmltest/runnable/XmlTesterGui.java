package com.stillwaterinsurance.xmltest.runnable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.xml.transform.TransformerException;

import com.stillwaterinsurance.xmltest.service.EngineService;
import com.stillwaterinsurance.xmltest.service.GuiPreferences;
import com.stillwaterinsurance.xmltest.util.XmlTestUtils;


public class XmlTesterGui extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(XmlTesterGui.class.getName());
	
	private JFileChooser fileChooser = new JFileChooser();

	private static final Map<Integer, String> engineUrls = setupEngineUrls();

	private JButton btnRun;
	@SuppressWarnings("rawtypes")
	private JComboBox cbEngineUrl;
	private JLabel jLabel1;
	private JSeparator jSeparator1;
	private JLabel lblEngineUrl;
	private JButton openButton;
	private TextArea receiveLog;
	private TextArea sendLog;
	private JLabel lblPrd;

	/**
	 * Creates new form XmlTesterGui
	 */
	@SuppressWarnings("rawtypes")
	public XmlTesterGui() {
		initComponents();

		Iterator<Entry<Integer, String>> iterator = engineUrls.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = iterator.next();
			if (entry.getValue().equals(GuiPreferences.getEngineUri())) {
				cbEngineUrl.setSelectedIndex((Integer) entry.getKey());
				break;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponents() {

		jSeparator1 = new JSeparator();
		receiveLog = new TextArea();
		sendLog = new TextArea();
		openButton = new JButton();
		cbEngineUrl = new JComboBox();
		jLabel1 = new JLabel();
		lblEngineUrl = new JLabel();
		btnRun = new JButton();
		lblPrd = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("XML tester");
		setLocation(new Point(50, 50));
		setMaximumSize(new Dimension(1113, 622));

		openButton.setText("Choose XML File");
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				openButtonActionPerformed(evt);
			}
		});

		cbEngineUrl.setModel(new DefaultComboBoxModel(new String[] { "local", "appqua01-oma", "omappqua", "omappint", 
				"omappply", "omappprd" }));
		cbEngineUrl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cbEngineUrlActionPerformed(evt);
			}
		});

		jLabel1.setText("Engine URL");

		lblEngineUrl.setText(GuiPreferences.getEngineUri());

		btnRun.setText("Run");
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				btnRunActionPerformed(evt);
			}
		});
		
		lblPrd.setForeground(Color.RED);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				receiveLog.setText("");
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addComponent(sendLog, GroupLayout.PREFERRED_SIZE, 640, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addComponent(btnRun)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup()
									.addGap(10)
									.addComponent(lblPrd))))
						.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(openButton)
							.addGap(66)
							.addComponent(jLabel1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEngineUrl)
								.addComponent(cbEngineUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(10)
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(receiveLog, GroupLayout.PREFERRED_SIZE, 640, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup()
							.addComponent(btnClear)
							.addContainerGap())))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(openButton)
						.addComponent(cbEngineUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel1))
					.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(sendLog, GroupLayout.PREFERRED_SIZE, 763, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup()
							.addComponent(btnRun)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPrd))))
				.addGroup(layout.createSequentialGroup()
					.addGap(23)
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnClear))
					.addGap(8)
					.addComponent(receiveLog, GroupLayout.PREFERRED_SIZE, 763, GroupLayout.PREFERRED_SIZE))
				.addGroup(layout.createSequentialGroup()
					.addContainerGap(22, Short.MAX_VALUE)
					.addComponent(lblEngineUrl)
					.addGap(781))
		);
		getContentPane().setLayout(layout);

		pack();
	}

	private void openButtonActionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			fileChooser.setCurrentDirectory(new File(GuiPreferences.getXmlStartPath()));
			final int returnVal = fileChooser.showOpenDialog(XmlTesterGui.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				GuiPreferences.setXmlStartPath(selectedFile.getPath());

				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(selectedFile));
					final StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line).append("\n");
					}

					final String formattedRequest = XmlTestUtils.formatXML(sb.toString());
					sendLog.setText(formattedRequest);

				} catch (final FileNotFoundException e) {
					System.out.println("File not found: " + selectedFile);
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (final TransformerException e) {
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

			}
		}
	}

	private void cbEngineUrlActionPerformed(ActionEvent evt) {
		String engineUrl = engineUrls.get(cbEngineUrl.getSelectedIndex());
		GuiPreferences.setEngineUri(engineUrl);
		lblEngineUrl.setText(engineUrl);
		lblPrd.setText("omappprd".equals(cbEngineUrl.getSelectedItem()) ? "PRD!!!" : "");
	}

	private void btnRunActionPerformed(ActionEvent evt){
		try {
			final String responseXml = EngineService.callWSEngine(sendLog.getText());
			final String formattedResponse = XmlTestUtils.formatXML(responseXml);
			if("".equals(receiveLog.getText())) {
				//This is needed to allow the clear button to always work. Not sure why, but if we append to 
				//an empty TextArea the clear button event won't fire correctly
				receiveLog.setText(formattedResponse);
			}
			else {
				receiveLog.append(formattedResponse);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new XmlTesterGui().setVisible(true);
			}
		});
	}
	
	
	private static Map<Integer, String> setupEngineUrls() {
		Map<Integer, String> urls = new HashMap<Integer, String>();
		int i = 0;
		urls.put(i++, "http://localhost:8080/WebServiceEngine/services/WSEngine/invoke");
		urls.put(i++, "http://appqua01-oma:8080/WebServiceEngine/services/WSEngine/invoke");
		urls.put(i++, "http://omappqua:8080/WebServiceEngine/services/WSEngine/invoke");
		urls.put(i++, "http://omappint:8080/WebServiceEngine/services/WSEngine/invoke");
		urls.put(i++, "http://omappply:8080/WebServiceEngine/services/WSEngine/invoke");
		urls.put(i++, "http://omappprd:8080/WebServiceEngine/services/WSEngine/invoke");

		return urls;
	}
}
