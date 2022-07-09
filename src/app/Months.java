package app;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JMonthChooser;

public class Months {

	private JList<String> CurrentMonth;
	private JList<String> NextMonth;
	private Vector<String> dates = new Vector<>();
	private Vector<String> fnames = new Vector<>();
	private Vector<String> lnames = new Vector<>();
	private Filefirstname ffn = new Filefirstname();
	private Filelastname fln = new Filelastname();
	private Filebirthdays fb = new Filebirthdays();
	private JScrollPane scrollPaneCM;
	private JScrollPane scrollPaneNM;
	private JFrame frame;
	private JTextField txtCurrentMonth;
	
	Months(JList<String> CurrentMonth, JList<String> NextMonth, JScrollPane scrollPaneCM, JScrollPane scrollPaneNM, JFrame frame){
		this.CurrentMonth = CurrentMonth;
		this.NextMonth = NextMonth;
		this.scrollPaneCM = scrollPaneCM;
		this.scrollPaneNM = scrollPaneNM;
		this.frame = frame;
	}
	
	void load() throws ParseException {
		SimpleDateFormat formatter_month = new SimpleDateFormat("MMMM");  
	    Date date = new Date(); 
	    
	    txtCurrentMonth = new JTextField();
		txtCurrentMonth.setEditable(false);
		txtCurrentMonth.setText(formatter_month.format(date)+"'s birthdays");
		txtCurrentMonth.setHorizontalAlignment(SwingConstants.CENTER);
		txtCurrentMonth.setBounds(10, 11, 250, 20);
		frame.getContentPane().add(txtCurrentMonth);
		txtCurrentMonth.setColumns(10);
		
		JMonthChooser monthChooser = new JMonthChooser();
		monthChooser.setBounds(341, 11, 108, 20);
		frame.getContentPane().add(monthChooser);
		
		scrollPaneCM = new JScrollPane();
		scrollPaneCM.setBounds(10, 41, 250, 250);
		frame.getContentPane().add(scrollPaneCM);
		
		scrollPaneNM = new JScrollPane();
		scrollPaneNM.setBounds(270, 41, 250, 250);
		frame.getContentPane().add(scrollPaneNM);
		
		try {
			dates = fb.getDates();
			fnames = ffn.getfname();
			lnames = fln.getlname();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		DefaultListModel<String> tempList = new DefaultListModel<String>();
		String tempLine;
		for(int i = 1; i < dates.size(); i++) {
			int cont = 0;
			Date temp = new SimpleDateFormat("dd MMMM").parse(dates.get(i));
			if(formatter_month.format(date).equals(formatter_month.format(temp))) {
				tempLine = dates.get(i) + " - " + fnames.get(i) + " " + lnames.get(i);
				tempList.add(cont, tempLine);
				cont++;
			}
		}
		OrderDaysMonth list = new OrderDaysMonth(tempList, dates, fnames, lnames);
		tempList = list.getOrder();
		CurrentMonth = new JList<String>(tempList);
		scrollPaneCM.setViewportView(CurrentMonth);
		
		int nextMonth = getNextMonth(formatter_month.format(date));
		monthChooser.setMonth(nextMonth);
		
		monthChooser.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				DefaultListModel<String> tempListNew = new DefaultListModel<String>();
				String tempLineNew;
				for(int i = 1; i < dates.size(); i++) {
					int contNew = 0;
					Date tempNew = null;
					try {
						tempNew = new SimpleDateFormat("dd MMMM").parse(dates.get(i));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(formatter_month.format(tempNew).equals(getMonth(monthChooser.getMonth()))) {		
						tempLineNew = dates.get(i) + " - " + fnames.get(i) + " " + lnames.get(i);
						tempListNew.add(contNew, tempLineNew);
						contNew++;
					}
				}
				OrderDaysMonth listNew = new OrderDaysMonth(tempListNew, dates, fnames, lnames);
				try {
					tempListNew = listNew.getOrder();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				NextMonth = new JList<String>(tempListNew);
				scrollPaneNM.setViewportView(NextMonth);
				new BtnDel().loadNext(NextMonth);
			}
		});
	}

	void birthday() throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM");  
	    Date date = new Date(); 
	    dates = fb.getDates();
		fnames = ffn.getfname();
		lnames = fln.getlname();
		for(int i = 1; i < dates.size(); i++) {
			if(formatter.format(date).equals(dates.get(i))) {
				System.out.println("Today is " + fnames.get(i) + " " + lnames.get(i) + "'birthday!");
			}
		}
	}
	
	String getMonth(int i) {
		String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		return months[i];
	}
	
	int getNextMonth(String currentMonth) {
		int nextMonth = 0;
		String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		for(int i=0; i<months.length; i++) {
			if(currentMonth.equals(months[i])) {
				if(i == months.length) {
					nextMonth = 0;
				} 
				else {
					nextMonth = i+1;
				}
			}
		}
		return nextMonth;
	}
	
	public JList<String> getCurrentMonth(){
		return CurrentMonth;
	}
}
