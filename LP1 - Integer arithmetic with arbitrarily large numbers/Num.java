package idsa;

import java.util.*;

public class Num implements Comparable<Num> {

    static long defaultBase = 10;  // Change as needed
    static long base = 1000000000;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative=false;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    int digits = 9;

    public Num(){
        base = 1000000000;
    }

    public Num(String s) {
        this.len = 0;
        int i;
        arr = new long[(s.length() / digits) + 1];
        if (s.charAt(0) == '-') {
            isNegative = true;
        }
        for (i = s.length(); i >= 9; i = i - digits) {
            this.arr[this.len] = Long.parseLong(s.substring(i - digits, i));
            len++;
        }
        if (i > 0) {
            if (isNegative) {
                this.arr[this.len] = Long.parseLong(s.substring(1, i));
            } else {
                this.arr[this.len] = Long.parseLong(s.substring(0, i));
            }
            len++;
        }
    }

    public Num(long x) {
        arr=new long[((Long.toString(x).length())/digits)+1];
        int i=0;
        while(x>=1){
            arr[len]=x%base;
            x=x/base;
            len++;
        }
    }

//    public Num(int size) {
//        this.len=size;
//        this.arr=new long[size];
//    }

    public static Num add(Num a, Num b) {
        long sum=0;
        long carry=0;
        int a_len=a.len;
        int b_len=b.len;
        int i=0;
        Num temp=new Num();
        temp.len=Math.max(a_len,b_len);
        temp.arr=new long[temp.len];

        if((a.isNegative && !b.isNegative)  || (!a.isNegative && b.isNegative) ){

            if(a.isNegative){
                a.isNegative = false;
                if(a.compareTo(b) > 0 ){
                    temp = Num.normalSubtract(a,b);
                    temp.isNegative = true;
                }else{
                    temp = Num.normalSubtract(b,a);
                    temp.isNegative = false;
                }

            }else{

                b.isNegative = false;
                if(b.compareTo(a) > 0 ){
                    temp = Num.normalSubtract(b,a);
                    temp.isNegative = true;
                }else{
                    temp = Num.normalSubtract(a,b);
                    temp.isNegative = false;
                }

            }

        } else {
            while(a_len>0||b_len>0) {
                sum = (carry + (a_len > 0 ? a.arr[a.len - a_len] : 0) + (b_len > 0 ? b.arr[b.len - b_len] : 0));
                temp.arr[i] = sum % Num.base;
                carry = sum / Num.base;
                i++;
                a_len--;
                b_len--;
            }
            if(carry!=0){
                Num ntemp=new Num();
                ntemp.len=temp.len+1;
                ntemp.arr=new long[ntemp.len];
                int j;
                for(j=0;j<temp.len;j++){
                    ntemp.arr[j]=temp.arr[j];
                }
                ntemp.arr[j]=carry;
                temp=ntemp;
            }
            temp.isNegative=a.isNegative?true:false;
        }
        return temp;
    }

    public static Num subtract(Num a, Num b) {

        Num result;
        int flag = 0;

        // (-) - (+)
        if(a.isNegative && !b.isNegative){
            result = Num.add(a,b);
            result.isNegative = true;

        // (+) - (-)
        }else if(!a.isNegative && b.isNegative){
            result = Num.add(a,b);
            result.isNegative=false;
        }else{

            // a > b
            if(a.compareTo(b) >= 0){

                result = Num.normalSubtract(a,b);
                result.isNegative = a.isNegative;

            // a < b
            }else{
                result = Num.normalSubtract(b,a);
                result.isNegative = !b.isNegative;
            }

        }
        return result;
    }

    public static Num normalSubtract(Num x, Num y){

        // x is greater than y => result is positive
        int  iX= 0,iY =0, index=0;

        Num output = new Num();
        output.len = Math.max(x.len,y.len);
        output.arr = new long[output.len];

        long borrow = 0;
        long minus ;

        while( iX < x.len && iY < y.len){

            if(borrow == 0){
                minus = x.arr[iX] - y.arr[iY];
            }else{
                minus = x.arr[iX] - y.arr[iY] - borrow;
            }

            if(minus >=0){
                output.arr[index] = minus;
                borrow = 0;

            }else{
                output.arr[index] = minus + base;
                borrow = 1; // setting borrow = 1
            }

            iX++;
            iY++;
            index++;
        }

        while(iX < x.len){
            if(borrow==1){
                if(x.arr[iX] == 0){
                    minus = base - 1;
                }
                else{
                    minus = x.arr[iX] - borrow;
                    borrow = 0;
                }

                output.arr[iX] = minus;

                iX++;
                index++;

            }else{

                output.arr[index] = x.arr[iX];
                borrow = 0;
                iX++; index++;

            }
        }

        return output;
    }

    public static Num product(Num a, Num b) {
        Num tempans=new Num();
        int i,j;
        for(i=0;i<a.len;i++){
            Num Sum=new Num();
            Sum.len=b.len+i;
            Sum.arr=new long[b.len+i];
            long carry=0L;
            long temp;
            for(int k=0;k<i;k++){
                Sum.arr[k]=0L%base;
            }
            for(j=0;j<b.len;j++){
                temp=a.arr[i]*b.arr[j]+carry;
                Sum.arr[j+i]=temp%base;
                carry=temp/base;
            }
            if(carry>0){
                int k;
                long[] tarr=new long[Sum.len+1];
                for(k=0;k<Sum.len;k++){
                    tarr[k]=Sum.arr[k];
                }
                tarr[k]=carry;
                Sum.arr=tarr;
                Sum.len=Sum.arr.length;
            }
            if(i==0){
                int k;
                long[] tarr=new long[Sum.len];
                for(k=0;k<Sum.len;k++){
                    tarr[k]=Sum.arr[k];
                }

                tempans.arr=tarr;
                tempans.len=tempans.arr.length;
            }
            else {
                tempans= Num.add(tempans,Sum);
            }
        }
        return tempans;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
        Num ans = new Num();
        if(n==0){
            return new Num(1L);
        }
        if(n%2==0){
            ans= product(power(a,n/2),power(a,n/2));
            return ans;
        }
        else if(n%2==1){
            ans= product(power(a,n/2),power(a,n/2));

            ans = product(ans,a);
            if(a.isNegative){
                ans.isNegative = true;
            }

            return ans;
        }
        return null;
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {

        boolean signA = a.isNegative;
        boolean signB = b.isNegative;

        a.isNegative = false;
        b.isNegative = false;
        int a_len=a.len;
        Num iszero= new Num("0");
        Num isone=new Num(1L);
        Num istwo=new Num(2L);
        Num isMinus=new Num("-1");
        if(b.compareTo(iszero)==0){
            throw new ArithmeticException("Cannot divide");
        }
        if(a.compareTo(iszero)==0){

            return iszero;
        }
        if(b.compareTo(isone)==0){
            return a;
        }

        if(a.compareTo(b)==0){

            if(signA == signB){

                return isone;
            }
            else{

                return isMinus;
            }
        }
//        int b_len=b.len;



        a.isNegative = false;
        b.isNegative = false;
        if(a.compareTo(b) < 0 ){
            return iszero;
        }else{
            Num high = new Num();
            high.len = a.len;
            high.arr = new long[high.len];
            for (int i = 0; i < a_len; i++) {
                high.arr[i] = a.arr[i];
            }
            Num low = new Num(1L);
            Num res;
            Num sum;
            Num mid = new Num();
            while (low.compareTo(high) < 0) {

                sum = Num.add(low, high);
                mid = sum.by2();
                if (mid.compareTo(low) == 0) {
                    break;
                }
                res = Num.product(mid, b);
                if (res.compareTo(a) == 0) {
                    break;
                } else if (res.compareTo(a) < 0) {
                    low = mid;
                } else {
                    high = mid;
                }
            }

            if(signA == signB ){
                mid.isNegative = false;
            }else {
                mid.isNegative = true;
            }
            return mid;
        }
    }

    // return a%b
    public static Num mod(Num a, Num b) {
        Num remainder =new Num();
        Num quo= Num.divide(a,b);
        Num prod= Num.product(quo,b);
        remainder=subtract(a,prod);
        return remainder;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        Num high = new Num();
//        high = a;
        high.len = a.len;
        high.arr = new long[high.len];
        for (int i = 0; i < a.len; i++) {
            high.arr[i] = a.arr[i];
        }
        Num low = new Num(1L);
        Num mid = new Num();

        while(low.compareTo(high)<0) {
            mid = Num.add(low, high).by2();
            if(low.compareTo(mid) == 0) break;

            Num immediate = Num.product(mid,mid);

            if((immediate).compareTo(a) == 0) {
                break;
            }

            else if(immediate.compareTo(a) > 0) {
                high = mid;
            }
            else {
                low = mid;
            }
        }
        return mid;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
        if(this.isNegative^other.isNegative){
            if(this.isNegative){
                return -1;
            }
            else {
                return 1;
            }
        }
        else {
            if(this.isNegative && other.isNegative){
                if(this.len<other.len){
                    return -1;
                }
                else if(this.len>other.len){
                    return 1;
                }
                else{
                    int i=this.len-1;
                    while(i>-1){
                        if(this.arr[i]<other.arr[i]){
                            return 1;
                        }
                        if(this.arr[i]>other.arr[i]){
                            return -1;
                        }
                        i--;
                    }
                }
            }
            else {
                if(this.len<other.len){
                    return -1;
                }
                else if(this.len>other.len){
                    return 1;
                }
                else{
                    int i=this.len-1;
                    while(i>-1){
                        if(this.arr[i]<other.arr[i]){
                            return -1;
                        }
                        if(this.arr[i]>other.arr[i]){
                            return 1;
                        }
                        i--;
                    }
                }
            }
        }
        return 0;
    }

    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i= len-1;i>=0;i--) {

            if(arr[i] != 0L){
                sb.append(arr[i]);

                if(i>0) sb.append(",");

                if(i==0){
                    sb.append("]");
                }
            }

        }

        System.out.println(sb.toString());
    }


    // Return number to a string in base 10
    public String toString() {

        StringBuilder sb = new StringBuilder();

//        this.arr = convertToOriginalBase();
//        this.len = this.arr.length;
        if(this.isNegative){
            sb.append("-");
        }

        for(int i= len-1;i>=0;i--){

            if(String.valueOf(arr[i]).length() != 9 && i!=len-1 ){
                int zeros = String.valueOf(arr[i]).length();

                while(9-zeros > 0){
                    sb.append("0");
                    zeros++;
                }
            }
            sb.append(this.arr[i]);
        }

        return sb.toString();
    }

    public long base() { return base; }


    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {


        Num base = new Num((newBase));

        double currentB = Math.log10(this.base);
        double newB = Math.log10(newBase);

//        Num base = new Num((newBase));
        int newLen = ((int)(currentB/newB) * this.len)+1;

        // store output
        Num out = new Num();
        out.len = newLen;
        out.arr =  new long[newLen];

        int index = 0;

        // copy current number object
        Num num = new Num();
        num.len = this.len;
        num.arr = new long[this.len+1];


        for(int i = 0 ; i < arr.length; i++){
            num.arr[i] = arr[i];
        }

        while(num.compareTo(new Num("0")) > 0){
            Num immediate = Num.mod(num,base);
            out.arr[index] = Long.parseLong(immediate.toString());
            num = Num.divide(num,base);
            index++;
        }

//        out.base = newBase;
        return out;
    }

    // Divide by 2, for using in binary search
    public Num by2() {

        long carry=0;
        Num bytwo=new Num();

        if (this.arr[len - 1] == 1) {
            bytwo.len = this.len - 1;
            carry = 1;
        }
        else{
            bytwo.len = this.len;
        }

        bytwo.arr=new long[this.len];

        int i=this.len-1;
        while(i>=0){
            long res=0;
            if(carry==1){
                res=base+this.arr[i];
            }
            else{
                res=this.arr[i];
            }
            bytwo.arr[i]=res/2;
            carry = (res%2==0)?0:1;
            i--;
        }
        return bytwo;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        // stack to store operands
        Stack<Num> operands = new Stack<>();

        // list to store operator
        List<String> operators = new ArrayList<>();

        operators.add("*");
        operators.add("+");
        operators.add("-");
        operators.add("/");
        operators.add("%");
        operators.add("^");

        for(int i=0 ; i < expr.length ; i++){
            if(!operators.contains(expr[i])){
                Num x = new Num(expr[i]);
                operands.push(x);
            }else{
                Num no2 = operands.pop();
                Num no1 = operands.pop();
                Num result = new Num();
                switch(expr[i]){
                    case "*":
                                result = Num.product(no1,no2);
                                break;
                    case "+":
                                result = Num.add(no1,no2);
                                break;
                    case "/" :
                                if(no1.compareTo(no2) > 0){
                                    result = Num.divide(no1,no2);
                                }else{
                                    result = Num.divide(no2,no1);
                                }

                                break;
                    case "-" :
                                result = Num.subtract(no1,no2);
                                break;
                    case "%" :
                                result = Num.mod(no1,no2);
                                break;
                    case "^" :
                                result = Num.product(no1,no2);
                                break;
                }
                operands.push(result);
            }
        }
        return operands.pop();
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {

        Stack<Num> operands = new Stack<>();
        Stack<String> operator = new Stack<>();

        // HashMap to store <operator, priority> pairs
        Map<String, Integer> uniqueOperators = new HashMap<>();
        uniqueOperators.put("+", 3);
        uniqueOperators.put("-", 3);
        uniqueOperators.put("*", 2);
        uniqueOperators.put("/", 2);
        uniqueOperators.put("^", 1);
        uniqueOperators.put("%", 2);
        uniqueOperators.put("(", 4);
        uniqueOperators.put(")", 4);

        for(int i =0 ; i< expr.length;i++){
//            System.out.println(i);
            if(expr[i].equals("(")){
                operator.push(expr[i]);
            }else if(!uniqueOperators.containsKey(expr[i])){
                Num immediate = new Num(expr[i]);
                operands.add(immediate);
            }else if(expr[i].equals(")")){
                while(!operator.isEmpty() && !operator.peek().equals("(")){
                    Num no2 = operands.pop();
                    Num no1 = operands.pop();
                    operands.push(Num.applyOp(no1,no2,operator.pop()));
                }
                // pop open brace
                if(!operator.isEmpty()){
                    operator.pop();
                }
            }else{
                while((!operator.isEmpty()) && (uniqueOperators.get(operator.peek()) <= uniqueOperators.get(expr[i]))){

                    Num num2 = operands.pop();
                    Num num1 = operands.pop();
                    operands.push(Num.applyOp(num1,num2,operator.pop()));
                }
                operator.push(expr[i]);
            }
        }

        while((!operator.isEmpty())){

            Num num2 = operands.pop();
            Num num1 = operands.pop();
            operands.push(Num.applyOp(num1,num2,operator.pop()));
        }
        return operands.pop();
    }

    // Function to perform arithmetic operations.
    public static Num applyOp(Num a, Num b, String op){
        switch(op){
            case "+": return Num.add(a,b);
            case "-": return Num.subtract(a,b);
            case "*": return Num.product(a,b);
            case "/": return Num.divide(a,b);
            case "^":
                    long power = Long.valueOf(b.toString());
                     return Num.power(a,power);
            case "%" : return Num.mod(a,b);
        }
        return null;
    }


    public static Num fibonacci(long n){

        Num a = new Num("0");
        Num b = new Num("1");
        Num c = new Num("1");

        for (int j=1 ; j<=n ; j++)
        {
            c = Num.add(a,b);
            a = b;
            b = c;
        }
        return a;
    }

    public static void main(String[] args) {

    }
}

