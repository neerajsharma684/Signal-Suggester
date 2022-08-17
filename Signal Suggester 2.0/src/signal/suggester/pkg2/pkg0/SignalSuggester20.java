package signal.suggester.pkg2.pkg0;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignalSuggester20 implements ActionListener{
    JFrame frame = new JFrame();
    JLabel welcome, select_market;
    JButton indian_stock, us_stock, crypto_spot, currencies;
    SignalSuggester20(){
        welcome = new JLabel("Welcome to Signal Suggester based on SMA/EMA crossover");
        select_market = new JLabel("Select Market:");
        welcome.setFont(new Font("Ariel", Font.BOLD, 26));
        welcome.setBounds(0, 10, 890, 50);
        welcome.setHorizontalAlignment(JLabel.CENTER);
        select_market.setFont(new Font("Ariel", Font.BOLD, 24));
        select_market.setBounds(0, 100, 890, 50);
        select_market.setHorizontalAlignment(JLabel.CENTER);
        
        indian_stock = new JButton("Indian Stocks");
        us_stock = new JButton("US Stocks");
        crypto_spot = new JButton("Cryptocurrencies");
        currencies = new JButton("Forex");
        indian_stock.setFont(new Font("Ariel", Font.BOLD, 16));
        us_stock.setFont(new Font("Ariel", Font.BOLD, 16));
        crypto_spot.setFont(new Font("Ariel", Font.BOLD, 16));
        currencies.setFont(new Font("Ariel", Font.BOLD, 16));
        
        indian_stock.addActionListener(this);
        us_stock.addActionListener(this);
        crypto_spot.addActionListener(this);
        currencies.addActionListener(this);
        
        indian_stock.setBounds(340, 200, 200, 25);
        us_stock.setBounds(340, 250, 200, 25);
        crypto_spot.setBounds(340, 300, 200, 25);
        currencies.setBounds(340, 350, 200, 25);
        
        frame.add(welcome);
        frame.add(select_market);
        frame.add(indian_stock);
        frame.add(us_stock);
        frame.add(crypto_spot);
        frame.add(currencies);
        frame.setTitle("Signal Suggester");
        frame.setSize(900, 500);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == indian_stock){
            Indian_Stocks.main(null);
            frame.dispose();
        }
        else if(e.getSource() == us_stock){
            US_Stocks.main(null);
            frame.dispose();
        }
        else if(e.getSource() == crypto_spot){
            Crypto_Spot.main(null);
            frame.dispose();
        }
        else if(e.getSource() == currencies){
            Forex.main(null);
            frame.dispose();
        }
    }
    
    public static void main(String[] args) {
        new SignalSuggester20();
    }   
}