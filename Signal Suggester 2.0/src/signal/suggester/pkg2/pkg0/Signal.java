package signal.suggester.pkg2.pkg0;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Signal {
    
    private long epoch(){
        long period = System.currentTimeMillis()/1000;
        return period;
    }
    
    public String Stock_Date(String Ticker) throws Exception{
        String Date = null, line;
        int ignore_first_line = 0;
        String[] command = {"curl", "\"https://query1.finance.yahoo.com/v7/finance/download/" + Ticker};
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while((line = reader.readLine()) != null)
        {
            String[] values = line.split(",");
            if(ignore_first_line > 0)
            {
                Date = values[0];
            }
            ignore_first_line++;
        }
        return Date;
    }
    public ArrayList<Double> fetch_data(String Ticker, int TimeDuration, String Interval) throws Exception{
        ArrayList<Double> close_price  = new ArrayList<>();
        String line;
        int ignore_first_line = 0;
        Signal obj = new Signal();
        long period_to = obj.epoch();
        long period_from = period_to - (86400 * TimeDuration);
        String command[] = {"curl" , "\"https://query1.finance.yahoo.com/v7/finance/download/" + Ticker + "?period1=" + period_from + "&period2=" + period_to + "&interval=" + Interval +"\""};
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while((line = reader.readLine()) != null)
        {
            String[] values = line.split(",");
            if(ignore_first_line>0)
            {
                if(!values[4].equals("null"))
                {
                    close_price.add(Double.parseDouble(values[4]));
                }
                else
                {
                    close_price.add(close_price.get(close_price.size()-1));
                }
                
            }
                ignore_first_line++;
        }
        return close_price;
    }
    
    public ArrayList<Double> sma_calculate(int Duration, ArrayList<Double> data){
        ArrayList<Double> sma = new ArrayList<>();
        Double sum = 0.0; 
        int count = 0;
        // Duration sma example: 20day SMA
        for(int i = 0; i < data.size(); i++)
        {
            if(count < Duration)
            {
                sum = sum + data.get(i);
                count ++;
            }
            else if(count == Duration)
            {
                sma.add(sum / Duration);
                sum = 0.0;
                count = 0;
                i = i - Duration;
            }
        }
        return sma;
    }
    
    public Double firstValue (ArrayList<Double> data, int Duration){
        Double value, sum = 0.0;
        int count = 0;
        while(count < Duration)
        {
            sum = sum + data.get(count);
            count++;
        }
        value = sum / Duration;
        return value;
    }
    
    public ArrayList<Double> ema_calculate(int Duration, Double firstValue, ArrayList<Double> data){
        ArrayList<Double> ema = new ArrayList<>();
        ema.add(firstValue);
        // Duration ema example: 20day ema
        for(int i = Duration + 1; i < data.size(); i++)
        {
            Double price = data.get(i);
            Double datedMultiplier = 2.0 / (Duration + 1);
            Double previousEMA = ema.get(ema.size() - 1);
            Double y = 1 - datedMultiplier;
            Double value = price * datedMultiplier + previousEMA * y;           //Formula to calculate EMA
            ema.add(value);
        }
        return ema;
    }
    
    public ArrayList<Double> dema_calculate(int Duration, ArrayList<Double> data){
        Double first = firstValue(data, Duration);
        ArrayList<Double> ema = ema_calculate(Duration, first, data);
        ArrayList<Double> ema2 = ema_calculate(Duration, ema.get(0), ema);
        ArrayList<Double> dema = new ArrayList<>();
        int difference = ema.size() - ema2.size();
        for (int i = 0; i < ema2.size(); i++)
        {
            dema.add(2 * ema.get(difference) - ema2.get(i));                //Formula to calculate DEMA
            difference++;
        }
        return dema;
    }
    
    public ArrayList<Double> tema_calculate(int Duration, ArrayList<Double> data){
        Double first = firstValue(data, Duration);
        ArrayList<Double> ema = ema_calculate(Duration, first, data);
        ArrayList<Double> ema2 = ema_calculate(Duration, ema.get(0), ema);
        ArrayList<Double> ema3 = ema_calculate(Duration, ema2.get(0), ema2);
        ArrayList<Double> tema = new ArrayList<>();
        int difference1 = ema.size() - ema3.size();
        int difference2 = ema2.size() - ema3.size();
        for(int i = 0; i < ema3.size(); i++)
        {
            tema.add((3 * ema.get(difference1)) - (3 * ema2.get(difference2)) + ema3.get(i));   //Formula to calculate TEMA
            difference1++;
            difference2++;
        }
        return tema;
    }
    
    public ArrayList<Double> macd_calculate(ArrayList<Double> data){
        Double first = firstValue(data, 12), second = firstValue(data, 26);
        ArrayList<Double> ema = ema_calculate(12, first, data);
        ArrayList<Double> ema2 = ema_calculate(26, second, data);
        ArrayList<Double> macd = new ArrayList<>();
        int difference = ema.size() - ema2.size();
        for(int i = 0; i < ema2.size(); i++)
        {
            macd.add(ema.get(difference) - ema2.get(i));            //Formula to calculate MCAD line
            difference++;
        }
        return macd;
    }
    
    public ArrayList<String> Signal_Find(ArrayList<Double> data1, ArrayList<Double> data2){
        ArrayList<String> Signal = new ArrayList<>();
        int Difference = data1.size() - data2.size();
        //Finding Buy/Sell/Neutral Signals
        for(int i = 0; i < data2.size(); i++)
        {
            if(data1.get(i + Difference) > data2.get(i))
            {
                Signal.add("Buy");
            }
            else if(data1.get(i + Difference) < data2.get(i))
            {
                Signal.add("Sell");
            }
            else
            {
                Signal.add("Neutral");
            }
        }

        //Filtering Signals
        for(int i = 0; i < Signal.size(); i++)
        {
            if((i+1) < (Signal.size() - 1))
            {
                if((Signal.get(i).equals("Buy") || Signal.get(i).equals("Hold Buy")) && Signal.get(i+1).equals("Buy"))
                {
                    Signal.set(i+1, "Hold Buy");
                }
                else if((Signal.get(i).equals("Sell") || Signal.get(i).equals("Hold Sell")) && Signal.get(i+1).equals("Sell"))
                {
                    Signal.set(i+1, "Hold Sell");
                }
                else if(Signal.get(i).equals("Neutral"))
                {
                    Signal.set(i, Signal.get(i-1));
                }
            }
            else if(Signal.get(i).equals("Buy") && Signal.get(i-1).equals("Hold Buy"))
            {
                Signal.set(i, "Hold Buy");
            }
            else if(Signal.get(i).equals("Sell") && Signal.get(i-1).equals("Hold Sell"))
            {
                Signal.set(i, "Hold Sell");
            }
        }
        return Signal;
    }
    
    public String Latest_Signal_Find(ArrayList<Double> data1, ArrayList<Double> data2){
        String finalSignal = null;
        ArrayList<String> signal = Signal_Find(data1, data2);
        finalSignal = signal.get(signal.size() - 1);
        return finalSignal;
    }
}
