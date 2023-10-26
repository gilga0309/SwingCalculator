import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class SwingCalculator {

	public static void main(String[] args) {
		Graphics cal = new Graphics();
		cal.Start();
	}

}

class Graphics implements ActionListener{

	TextField calbox;
	String value = "";
	String[] name = {"7","8","9","/","4","5","6","*","1","2","3","-","C","0","=","+"};
	Button[] button = new Button[name.length];
	
	public void Start() {
		
		Frame f = new Frame("Calculator");
		
		f.setSize(400,490);
		f.setLayout(null);
		
		Panel p1 = new Panel();
		Panel p2 = new Panel();
		
		p1.setSize(360,80);
		p1.setLocation(20,30);
		
		p2.setSize(360,360);
		p2.setLocation(20,110);
		p2.setLayout(new GridLayout(4,4));
		f.add(p1);
		f.add(p2);
		
		calbox =new TextField(12);
		calbox.setFont(new Font("맑은고딕", Font.BOLD, 50));
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(calbox);
		p1.setVisible(true);
		
		for(int i=0; i<name.length; i++) {
			button[i] = new Button(name[i]);
			button[i].setBackground(Color.white);
			button[i].setFont(new Font("맑은고딕", Font.BOLD, 30));
			p2.add(button[i]);
		}
		for(int i=0; i<name.length; i++)
			button[i].addActionListener(this);
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		f.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		for(int i=0; i<name.length; i++) {
			if(name[i].equals(e.getActionCommand())) {
				button[i].setBackground(Color.gray);
				if(name[i]!="C" && name[i]!="=") {
					value+=name[i];
					calbox.setText(value);//("7");
				}
				if(name[i]=="*" || name[i]=="/" || name[i]=="+" || name[i]=="-")
					reset();
				if(name[i]=="C") {
					reset();
					value="";
					calbox.setText(value);
				}
				try {
				if(name[i]=="=") {
					reset();
					Result re = new Result();
					value=re.Result(value);
					calbox.setText(value);
				}
				}catch(Exception d) {
					calbox.setText("에러 입니다.");
				}
			}
		}	
	}
	public void reset() {
		for(int i=0; i<name.length; i++) {
			button[i].setBackground(Color.white);
		}
	}
}

class Result {
	
	static Stack st = new Stack();
	
	String Result(String input){// Calculator 생성자
    	
		int sign1=0, sign2=0, cheak=0;
		String change="";
		int division=0;
    	loop1 : for(int i=0; i<input.length(); i++){
    		if(sign2!=0){
				sign1=sign2+1;
			}
    		if((input.charAt(i)=='+' || input.charAt(i)=='-' || input.charAt(i)=='*' || input.charAt(i)=='/') && i>0) {//if문에서 +,-,*,/ 구별
    			sign2=i;
				st.push(input.substring(sign1,sign2));
				if(input.charAt(i)=='*' || input.charAt(i)=='/'){// *,/ 일때, division 변수 1로 변환
					division=1;
				}
				if(input.charAt(i)=='+' || input.charAt(i)=='-'){// *,/ 가 체크 된 뒤 +,- 일때, 앞뒤 변환후 계산
					if(division==1){
						division=0;
						change="";
						while(!st.empty()){
							change += (String)st.pop();
						}
						change=exchange(change);
						input = input.replace(change, emptycal(change));
						sign1=0; sign2=0; i=0; change="";
						continue loop1;
					}
				}
				st.push(input.substring(sign2,sign2+1));
        	}
    		
    		if(i+1==input.length()-1){//마지막 계산
				String num1="", num2="", str="";
				
				st.push(input.substring(sign2+1));
				
				if(division==1){// *,/가 있을때
					division=0;
					while(!st.empty()){
						change += (String)st.pop();
					}
					change=exchange(change);
					input = input.replace(change, emptycal(change));
					//System.out.println("finish result : "+input);
					if(emptycal(change).length()==input.length()){
						break;
					}
				}
				
				while(!st.empty()){// *,/가 없을때
					if(cheak==0){
						num1=(String)st.pop();
					}else if(cheak==1){
						str=(String)st.pop();
					}else if(cheak==2){
						num2=(String)st.pop();
						num1 = formula(num2,num1,str);
						cheak=0;
					}
					cheak++;
				}
				input = num1;
				return input;
			}
    	}
		return input;
	}
	
	static String emptycal(String ex){
        int sign1, sign2, sign3, select = 0;
        // *곱하기 /나누기
        for (int i = 0; i < ex.length(); i ++) {
            if (ex.charAt(i) == '*' || ex.charAt(i) == '/') 
                select = 1;
        }
        switch (select) {
            case 1:
                {
                    sign1 = 0; sign2 = 0; sign3 = 0;
                    for (int i = 0; i < ex.length(); i ++) {
                        if ((ex.charAt(i) == '*' || ex.charAt(i) == '/') && sign2 == 0) 
                            sign2 = i;
                        if ((ex.charAt(i) == '+' || ex.charAt(i) == '-' || ex.charAt(i) == '*' || ex.charAt(i) == '/') && sign2 != 0 && sign2 < i) {
                            sign3 = i;
                            break;
                        } else if (sign2 != 0 && sign2 < i && i == ex.length() - 1) {
                            sign3 = i + 1;
                            break;
                        }
                    }
                    sign1 = Searchsign1(ex, sign2);
                    if (sign1 == 0 && sign2 == 0 && sign3 == 0) {
                        return ex;
                    }
                    if (ex.charAt(sign2) == '*') {
                        ex = ex.replace(ex.substring(sign1, sign3), formula( ex.substring(sign1, sign2),ex.substring(sign2+1, sign3),"*"));
                    } else if (ex.charAt(sign2) == '/') {
                        ex = ex.replace(ex.substring(sign1, sign3), formula( ex.substring(sign1, sign2),ex.substring(sign2+1, sign3),"/"));
                    }
                    return emptycal(ex);
                }
            case 0:
                {
                    sign1 = 0; sign2 = 0; sign3 = 0;
                    for (int i = 0; i < ex.length(); i ++) {
                        if ((ex.charAt(i) == '+' || ex.charAt(i) == '-') && sign2 == 0) 
                            sign2 = i;
                        if ((ex.charAt(i) == '+' || ex.charAt(i) == '-' || ex.charAt(i) == '*' || ex.charAt(i) == '/') && sign2 != 0 && sign2 < i) {
                            sign3 = i;
                            break;
                        } else if (sign2 != 0 && sign2 < i && i == ex.length() - 1) {
                            sign3 = i + 1;
                            break;
                        }
                    }
                    sign1 = Searchsign1(ex, sign2);
                    if (sign1 == 0 && sign2 == 0 && sign3 == 0) {
                        return ex;
                    }
                    if (ex.charAt(sign2) == '+') {
                        ex = ex.replace(ex.substring(sign1, sign3), formula( ex.substring(sign1, sign2),ex.substring(sign2 + 1, sign3),"+"));
                    } else if (ex.charAt(sign2) == '-') {
                        ex = ex.replace(ex.substring(sign1, sign3), formula( ex.substring(sign1, sign2),ex.substring(sign2 + 1, sign3),"-"));
                    }
                }
                return emptycal(ex);
            }
		return ex;
	}
	
	static String formula(String for1, String for2, String sign){// 계산값1, 계산값2, 계산 부호
		String result="";
		
		if(sign.equals("+")){
			result = String.valueOf(Float.valueOf(for1)+Float.valueOf(for2));
		}else if(sign.equals("-")){
			result = String.valueOf(Float.valueOf(for1)-Float.valueOf(for2));
		}else if(sign.equals("*")){
			result = String.valueOf(Float.valueOf(for1)*Float.valueOf(for2));
		}else{
			result = String.valueOf(Float.valueOf(for1)/Float.valueOf(for2));
		}
		return result;
	}
	
	static String exchange(String input){//앞 뒤 전환
		String[] box = new String[input.length()];
		
		String str="";
		int j=0;
		for(int i1=0,i2=0; i2<input.length(); i2++){
			if(input.charAt(i2)=='+' || input.charAt(i2)=='-' || input.charAt(i2)=='*' || input.charAt(i2)=='/'){
				box[j++]=input.substring(i1, i2);
				box[j++]=input.substring(i2, i2+1);
				i1=i2+1;
			}
			if(i2==input.length()-2){
				box[j++]=input.substring(i1);
			}
		}
		for(; j<box.length; j++){
			box[j]="";
		}
		for(int i=input.length()-1; i>=0; i--){
			str+=box[i];
		}
		return str;
	}
	
	static int Searchsign1(String text, int sign2) {//text값에서 sign2길이 까지의 부호 검색
        int num = 0;
        for (int i = 0; i < sign2; i ++) {
            if (text.charAt(i) == '+' || text.charAt(i) == '-' || text.charAt(i) == '*' || text.charAt(i) == '/') 
                num = i + 1;
        }
        return num;
    }
}