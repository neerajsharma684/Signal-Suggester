package signal.suggester.pkg2.pkg0;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Forex implements ActionListener{
    Connection con= null;
    Statement stmt = null;
    JFrame frame = new JFrame();
    JLabel intro, enterTicker, hint, optional, basedOn, timeFrame, interval;
    JTextField inputTicker;
    JComboBox time_Frame, Interval, indicatorList, indicatorPeriod;
    JButton showTicker, reset, addIndicator, removeIndicator;
    JTable resultContainer;
    DefaultTableModel tableModel;
    JScrollPane scrollResult;
    Forex(){
        intro = new JLabel("Suggest By Ticker");
        intro.setFont(new Font("Ariel", Font.BOLD, 24));
        intro.setBounds(0, 30, 800, 30);
        intro.setHorizontalAlignment(JLabel.CENTER);
        enterTicker = new JLabel("Enter Ticker: ");
        hint = new JLabel("To add multiple Ticker add coma (,) with currency name Eg.(USD-INR, INR-JPY)");
        optional = new JLabel("Optional: ");
        basedOn = new JLabel("Based on: ");
        String[] indicator_Values = {"SMA Crossover", "EMA CrossOver", "DEMA Crossover", "TEMA Crossover", "MACD Crossover"};
        String[] indicatorPeriod_Values = {"9, 21 Period", "20, 50 Period", "25, 50 Period", "50, 100 Period", "100, 200 Period"};
        indicatorList = new JComboBox(indicator_Values);
        indicatorPeriod = new JComboBox(indicatorPeriod_Values);
        timeFrame = new JLabel("Time Frame: ");
        String[] timeFrame_values = {"1month", "3month", "6month", "1year", "2year", "3year", "5year"};
        time_Frame = new JComboBox(timeFrame_values);
        interval = new JLabel("Interval: ");
        String[] interval_values = {"1day", "5day", "1week", "1month", "3month"};
        Interval = new JComboBox(interval_values);
        
        enterTicker.setFont(new Font("Ariel", Font.BOLD, 18));
        hint.setFont(new Font("Ariel", Font.BOLD, 12));
        basedOn.setFont(new Font("Ariel", Font.PLAIN, 16));
        indicatorList.setFont(new Font("Ariel", Font.PLAIN, 16));
        indicatorPeriod.setFont(new Font("Ariel", Font.PLAIN, 16));
        timeFrame.setFont(new Font("Ariel", Font.PLAIN, 16));
        time_Frame.setFont(new Font("Ariel", Font.PLAIN, 16));
        interval.setFont(new Font("Ariel", Font.PLAIN, 16));
        Interval.setFont(new Font("Ariel", Font.PLAIN, 16));
        optional.setFont(new Font("Ariel", Font.BOLD, 18));
        time_Frame.setFont(new Font("Ariel", Font.PLAIN, 16));
        enterTicker.setBounds(150, 100, 150, 20);
        hint.setBounds(300, 120, 500, 20);
        optional.setBounds(150, 160, 150, 20);
        basedOn.setBounds(300, 175, 90, 20);
        indicatorList.setBounds(415, 160, 150, 25);
        indicatorPeriod.setBounds(415, 190, 150, 25);
        timeFrame.setBounds(300, 230, 110, 20);
        time_Frame.setBounds(415, 230, 100, 25);
        interval.setBounds(300, 270, 110, 20);
        Interval.setBounds(415, 270, 100, 25);
        
        time_Frame.setSelectedIndex(4);
        Interval.setSelectedIndex(0);
        
        inputTicker = new JTextField();
        inputTicker.setFont(new Font("Ariel", Font.PLAIN, 16));
        inputTicker.setBounds(300, 100, 275, 20);

        showTicker = new JButton("Show");
        reset = new JButton("Reset");
        addIndicator = new JButton("Add");
        removeIndicator = new JButton("Remove");
        showTicker.setFont(new Font("Ariel", Font.BOLD, 18));
        reset.setFont(new Font("Ariel", Font.BOLD, 18));
        addIndicator.setFont(new Font("Ariel", Font.PLAIN, 16));
        removeIndicator.setFont(new Font("Ariel", Font.PLAIN, 16));
        addIndicator.setBounds(580, 175, 65, 20);
        removeIndicator.setBounds(650, 175, 95, 20);
        showTicker.setBounds(300, 300, 100, 30);
        reset.setBounds(450, 300, 100, 30);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Date");
        tableModel.addColumn("Name");
        tableModel.addColumn("Price");
        
        resultContainer = new JTable(tableModel);
        resultContainer.setFont(new Font("Ariel", Font.BOLD, 12));
        resultContainer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollResult = new JScrollPane(resultContainer);
        resultContainer.setDefaultEditor(Object.class, null);
        scrollResult.setBounds(10, 350, 770, 280);
        
        showTicker.addActionListener(this);
        reset.addActionListener(this);
        addIndicator.addActionListener(this);
        removeIndicator.addActionListener(this);        
        frame.getRootPane().setDefaultButton(showTicker);
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/signal_suggester", "root", "root");
            stmt = con.createStatement();
        }
        catch(SQLException e){}
        frame.add(intro);
        frame.add(enterTicker);
        frame.add(inputTicker);
        frame.add(hint);
        frame.add(optional);
        frame.add(basedOn);
        frame.add(indicatorList);
        frame.add(indicatorPeriod);
        frame.add(addIndicator);
        frame.add(removeIndicator);
        frame.add(timeFrame);
        frame.add(time_Frame);
        frame.add(interval);
        frame.add(Interval);
        frame.add(showTicker);
        frame.add(reset);
        frame.add(scrollResult);
        frame.setSize(800,700);
        frame.setTitle("Signal Suggester - Forex");
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SignalSuggester20.main(null);
                try
                {
                    if(con != null)
                    {
                        con.close();
                    }
                }
                catch(SQLException e1){}
                frame.dispose();
            }
        });
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public void singleTicker(String Ticker, int FinalTimeFrame, String finalInterval){
        Signal obj = new Signal();
        ArrayList<Double> closePrice = new ArrayList<>();
        String date = null;
        Ticker = Ticker.replace("-", "");
        Ticker = Ticker.concat("=X");
        System.out.println(Ticker);
        try
        {
            closePrice = obj.fetch_data(Ticker, FinalTimeFrame, finalInterval);
            date = obj.Stock_Date(Ticker);
        }
        catch(Exception e){}
        int row = tableModel.getRowCount();
        tableModel.insertRow(row, new Object[] {date, Ticker, Math.round(closePrice.get(closePrice.size() - 1) * 100) / 100.0});
        if(tableModel.getColumnCount() > 3 && closePrice.size() > 0)
        {
            for(int i = 3; i < tableModel.getColumnCount(); i++)
            {
                String[] temp = tableModel.getColumnName(i).split(" ");
                String[] Period = tableModel.getColumnName(i).split(",");
                String[] cut = Period[0].split(" ");
                Period[0] = cut[1];
                if(temp[0].equals("SMA"))
                {
                    ArrayList<Double> sma1 = obj.sma_calculate(Integer.parseInt(Period[0]), closePrice);
                    ArrayList<Double> sma2 = obj.sma_calculate(Integer.parseInt(Period[1]), closePrice);
                    String signal = obj.Latest_Signal_Find(sma1, sma2);
                    tableModel.setValueAt(signal, row, i);
                }
                if(temp[0].equals("EMA"))
                {
                    Double first = obj.firstValue(closePrice, Integer.parseInt(Period[0]));
                    Double second = obj.firstValue(closePrice, Integer.parseInt(Period[1]));
                    ArrayList<Double> ema1 = obj.ema_calculate(Integer.parseInt(Period[0]), first, closePrice);
                    ArrayList<Double> ema2 = obj.ema_calculate(Integer.parseInt(Period[1]), second, closePrice);
                    String signal = obj.Latest_Signal_Find(ema1, ema2);
                    tableModel.setValueAt(signal, row, i);
                }
                if(temp[0].equals("DEMA"))
                {
                    ArrayList<Double> dema1 = obj.dema_calculate(Integer.parseInt(Period[0]), closePrice);
                    ArrayList<Double> dema2 = obj.dema_calculate(Integer.parseInt(Period[1]), closePrice);
                    String signal = obj.Latest_Signal_Find(dema1, dema2);
                    tableModel.setValueAt(signal, row, i);
                }
                if(temp[0].equals("TEMA"))
                {
                    ArrayList<Double> tema1 = obj.tema_calculate(Integer.parseInt(Period[0]), closePrice);
                    ArrayList<Double> tema2 = obj.tema_calculate(Integer.parseInt(Period[1]), closePrice);
                    String signal = obj.Latest_Signal_Find(tema1, tema2);
                    tableModel.setValueAt(signal, row, i);
                }
//                if(temp[0].equals("MACD"))
//                {
//                    ArrayList<Double> macd = obj.macd_calculate(closePrice);
//                    ArrayList<Double> ema = obj.ema_calculate(9, Double.MIN_VALUE, macd)
//                }
            }
        }
    }
    
    public void multipleTicker(String[] List, int FinalTimeFrame, String finalInterval){
        for(String Ticker : List)
        {
            singleTicker(Ticker, FinalTimeFrame, finalInterval);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == addIndicator)
        {
            int IndicatorName = indicatorList.getSelectedIndex();
            int IndicatorPeriod = indicatorPeriod.getSelectedIndex();
            String finalName = null;
            switch(IndicatorName)
            {
                case 0 -> finalName = "SMA";
                case 1 -> finalName = "EMA";
                case 2 -> finalName = "DEMA";
                case 3 -> finalName = "TEMA";
                case 4 -> finalName = "MACD";
            }
            if(!finalName.equals("MACD"))
            {
                switch(IndicatorPeriod)
                {
                    case 0 -> finalName = finalName.concat(" 9,21");
                    case 1 -> finalName = finalName.concat(" 20,50");
                    case 2 -> finalName = finalName.concat(" 25,50");
                    case 3 -> finalName = finalName.concat(" 50,100");
                    case 4 -> finalName = finalName.concat(" 50,100");
                }
            }
            tableModel.addColumn(finalName);
        }
        else if(e.getSource() == removeIndicator)
        {
            if(tableModel.getColumnCount() > 3)
            {
                tableModel.setColumnCount(tableModel.getColumnCount() - 1);
            }
        }
        else if(e.getSource() == showTicker)
        {
            String Ticker = inputTicker.getText().trim().toUpperCase();
            int TimeFrame = time_Frame.getSelectedIndex();
            int FinalTimeFrame = 0;
            switch(TimeFrame)
            {
                case 0 -> FinalTimeFrame = 30;
                case 1 -> FinalTimeFrame = (30 * 3);
                case 2 -> FinalTimeFrame = (30 * 6);
                case 3 -> FinalTimeFrame = 365;
                case 4 -> FinalTimeFrame = (365 * 2);
                case 5 -> FinalTimeFrame = (365 * 3);
                case 6 -> FinalTimeFrame = (365 * 5);
            }
            int tempInterval = Interval.getSelectedIndex();
            String finalInterval = null;
            switch(tempInterval)
            {
                case 0 -> finalInterval = "1d";
                case 1 -> finalInterval = "5d";
                case 2 -> finalInterval = "1wk";
                case 3 -> finalInterval = "1mo";
                case 4 -> finalInterval = "3mo";
            }
                String[] List;
                if(Ticker.contains(","))
                {
                    List = Ticker.split(",");
                    for(int i = 0; i < List.length; i++)
                    {
                        List[i] = List[i].trim().toUpperCase();
                    }
                    multipleTicker(List, FinalTimeFrame, finalInterval);
                }
                else
                {
                    singleTicker(Ticker, FinalTimeFrame, finalInterval);
                }
        }
        else if(e.getSource() == reset)
        {
            inputTicker.setText("");
            tableModel.setRowCount(0);
            indicatorList.setSelectedIndex(0);
            indicatorPeriod.setSelectedIndex(0);
        }
    }
    
    public static void main(String[] args){
        new Forex();
    }
}
