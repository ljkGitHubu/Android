package com.ljk;


import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ljk.databinding.ActivityMainBinding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    //初始化
    public String input = "";
    public String input1 = "";
    public int str = 0;
    public String res = "0";

    void BigDecimal(String String){
        BigDecimal(String);
    }

    public ArrayList<String> inputChar = new ArrayList<>(); //用来存放用户的输入 ，每点一次按钮存一次
    public ArrayList<String> sum = new ArrayList<>();  //用来存储多位数字
    public Stack<String>  cha = new Stack<>(); //用来存储读取到的符号，用于逆波兰的创建
    public ArrayList<String> nibo = new ArrayList<>(); //存放逆波兰式
    public Stack<Double>  ops = new Stack<>(); //存放运算栈
    public Map<String, Integer> map = new HashMap<>();    /*用于存四则运算符号的优先级*/

    //数字为多位的时候 存入到sum中，用sunmation计算再放到逆波兰中
    public void summation(){
        String num = "";
        for(int i=0;i<sum.size();i++){
            num += sum.get(i);
        }
        nibo.add(num);
        sum.clear();
    }

//测试逆波兰式
    public void  count() {
        for (int i=0;i<nibo.size();i++){

            Log.d("ljkjk12",nibo.get(i));
        }
        for (int i=0;i<inputChar.size();i++){
            Log.i("ljkjk1", "符号栈："+inputChar.get(i));
        }
    }

    //中缀式转逆波兰式
    public void tran() {
        //定义优先级
        map.put("+",1);
        map.put("-",1);
        map.put("*",2);
        map.put("/",2);
        map.put("s",3);
        map.put("c",3);
        map.put("t",3);
        map.put("l",3);
        map.put("√",3);
        map.put("!",3);
        map.put("^",3);
        map.put("(",4);
        map.put(")",4);



        //将inputStr转逆波兰
        for(int i=0;i<inputChar.size();i++){
            String nowStr = inputChar.get(i);
            if(nowStr.equals("+") || nowStr.equals("-") || nowStr.equals("*") || nowStr.equals("/") || nowStr.equals("(")|| nowStr.equals(")") || nowStr.equals("s") || nowStr.equals("c") || nowStr.equals("t") ||nowStr.equals("l")|| nowStr.equals("√") || nowStr.equals("!") || nowStr.equals("^")) {
                if (sum.size() != 0) {
                    summation();
                }// /*入栈之前要先将存储在sum里的数据输出出来*/
                //符号栈为空，或者当前符号优先级大于栈顶，或者栈顶为(,直接入栈；
                if ((cha.isEmpty() || map.get(nowStr) > map.get(cha.peek()) || (cha.peek()).equals("(") || nowStr.equals("("))) {
                    if (nowStr.equals(")")) {
                        while (!(cha.peek()).equals("(")) {
                            nibo.add(cha.pop());
                        }//遇到右括号时，要把括号之间的符号全部pop到逆波兰中，匹配第一个左括号
                        cha.pop();
                        continue;
                    }
                    cha.push(nowStr);
                }//符号栈为空，直接入栈
                else {
                    while (!cha.isEmpty() && !(cha.peek()).equals("(") )
                    {
                    if (map.get(nowStr)<=map.get(cha.peek())) {
                        nibo.add(cha.pop());
//                        break;
                    }//当前符号优先级小于栈顶，栈顶出栈
                }
                    cha.push(nowStr);
            }
//                continue;
        }
            else sum.add(nowStr);//放到sum中 summation计算转换成一个整体

        }
        if(!sum.isEmpty()){ summation();}//将最后的数字加入nibo
        while (!cha.isEmpty()){nibo.add(cha.pop());}//剩着的字符入nibo

    }

    //计算式子
    public void jisuan () {
        for (int i=0;i<nibo.size();i++){
            String nowChar = nibo.get(i);
                switch (nowChar) {
                    case "+":
                    case "-":
                    case "*":
                    case "/": {
                        Double one = ops.pop();
                        Double two = ops.pop();
                        if (nowChar.equals("+")) {
                            ops.push(two + one);
//                            ops.push(one.0add(two));
                        }

                        if (nowChar.equals("-")) {
                            ops.push(two - one);
                        }
                        if (nowChar.equals("*")) {
                            ops.push(two * one);
                        }
                        if (nowChar.equals("/")) {
                            if (one==0)
                                Toast.makeText(MainActivity.this,"除数不能为0！",Toast.LENGTH_LONG).show();
                            else
                                ops.push(two / one);
                        }
                        break;
                    }
                    case "s":
                    case "c":
                    case "t":
                    case "√": {
//                        BigDecimal one = new BigDecimal(Double.toString(ops.pop()));
//                        BigDecimal c = new BigDecimal(Double.toString(ops.pop()));
                        double one = ops.pop();
                        double c = Math.toRadians(one);
                        if (nowChar.equals("s")) {
                            ops.push(Math.sin(c));
//                            ops.push(Math.sin(c));
                        }
                        if (nowChar.equals("c")) {
                            ops.push(Math.cos(c));
                        }
                        if (nowChar.equals("t")) {
                            ops.push(Math.tan(c));
                        }
                        if (nowChar.equals("√")) {
                                ops.push(Math.sqrt(one));
                        }
                        break;
                    }
                    case "^": {
                        double one = ops.pop();
                        double two = ops.pop();
                        ops.push(Math.pow(two, one));
                        break;
                    }
                    case "!": {
                        Double one = ops.pop();
                        double j = 1.0;
                        for (int m = 1; m <= one; m++) {
                            j = j*m;
                        }
                        ops.push(j);
                        break;
                    }
                    default:
                        ops.push(Double.parseDouble(nowChar));
                        break;
                }

        }
    }

    //使用javax.script,需要在app的gradle中添加依赖项implementation 'io.apisense:rhino-android:1.0'
//    ScriptEngineManager manager = new ScriptEngineManager();
//    ScriptEngine engine = manager.getEngineByName("js");


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setTitle("1743205000210李金康");

        //使用viewBinding ，需要在app的gradle中添加viewBinding{enabled = true}
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //添加所有的点击事件
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //只输入数字就加上去
                    case R.id.s0:
                        binding.top.append("0");
                        input += ((Button)v).getText().toString();
                        inputChar.add("0");
                        break;
                    case R.id.s1:
                        binding.top.append("1");
                        input += ((Button)v).getText().toString();
                        inputChar.add("1");
                        break;
                    case R.id.s2:
                        binding.top.append("2");
                        input += ((Button)v).getText().toString();
                        inputChar.add("2");
                        break;
                    case R.id.s3:
                        binding.top.append("3");
                        input += ((Button)v).getText().toString();
                        inputChar.add("3");
                        break;
                    case R.id.s4:
                        binding.top.append("4");
                        input += ((Button)v).getText().toString();
                        inputChar.add("4");
                        break;
                    case R.id.s5:
                        binding.top.append("5");
                        input += ((Button)v).getText().toString();
                        inputChar.add("5");
                        break;
                    case R.id.s6:
                        binding.top.append("6");
                        input += ((Button)v).getText().toString();
                        inputChar.add("6");
                        break;
                    case R.id.s7:
                        binding.top.append("7");
                        input += ((Button)v).getText().toString();
                        inputChar.add("7");
                        break;
                    case R.id.s8:
                        binding.top.append("8");
                        input += ((Button)v).getText().toString();
                        inputChar.add("8");
                        break;
                    case R.id.s9:
                        binding.top.append("9");
                        input += ((Button)v).getText().toString();
                        inputChar.add("9");
                        break;
                    case R.id.s_e:
                        binding.top.append("e");
                        input += ((Button)v).getText().toString();
                        inputChar.add("e");
                        break;
                    case R.id.s_leftc:
                        binding.top.append("(");
                        input += ((Button)v).getText().toString();
                        inputChar.add("(");
                        break;
                    case R.id.s_rightc:
                        binding.top.append(")");
                        input += ((Button)v).getText().toString();
                        inputChar.add(")");
                        break;
                    case R.id.s_log:
                        binding.top.append("log");
                        input += ((Button)v).getText().toString();
                        inputChar.add("l");
                        break;
                    case R.id.s_dian:
                        binding.top.append(".");
                        input += ((Button)v).getText().toString();
                        inputChar.add(".");
                        break;
//                        binding.top.append(((Button)v).getText());//显示输入的内容
//                        input += ((Button)v).getText().toString();
////                        input2 += ((Button)v).getText().toString();
//                        //使用input1来判断学号姓名的输出
//                        input1 += ((Button)v).getText().toString();
//
//
//                        binding.result.setText("");//只输入数字，结果等于本身
//                        binding.result.append("="+res);
//                        break;

                    case R.id.s_tan:
                        binding.top.append(((Button)v).getText());
                        input += ((Button)v).getText().toString();
                        inputChar.add("t");
                        break;
                    case R.id.s_cos:
                        binding.top.append(((Button)v).getText());
                        input += ((Button)v).getText().toString();
                        inputChar.add("c");
                        break;
                    case R.id.s_sin:
                        binding.top.append(((Button)v).getText());
                        input += ((Button)v).getText().toString();
                        inputChar.add("s");
                        break;

                    case R.id.s_mi:
                        binding.top.append("^");
                        input += "^";
//                        input = input+"^";
                        inputChar.add("^");
                        break;
                    case R.id.s_jiecheng:
                        binding.top.append("!");
                        input += "!";
//                        input = input+"!";
                        inputChar.add("!");
                        break;
                    case R.id.s_sqrt:
                        binding.top.append("√");
                        input += "√";
//                        input = input+"Math.sqrt(";
                        inputChar.add("√");
                        break;
                    case R.id.s_a:
                        binding.top.append("+");
                        input += "+";
                        inputChar.add("+");
                        break;
                    case R.id.s_j:
                        binding.top.append("-");
                        input += "-";
                        inputChar.add("-");
                        break;
                    case R.id.s_cheng:
                        binding.top.append("*");
                        input += "*";
                        inputChar.add("*");
                        break;
                    case R.id.s_chu:
                        binding.top.append("/");
                        input += "/";
                        inputChar.add("/");
                        break;
//                        binding.top.append(((Button)v).getText());//符号也显示到textview中
//                        //输入符号之后 str变化，输出改变
//                        str += 2;
//                        input = input+((Button)v).getText();//算式字符串也跟着改变
//                        inputChar.add(((Button)v).getText().toString());

                        //输入符号之后，字体，颜色再变回来，重新点击等号再重新改变
//                        binding.top.setTextSize(50);
//                        binding.result.setTextSize(40);
//                        binding.top.setTextColor(Color.parseColor("#000000"));
//                        binding.result.setTextColor(Color.parseColor("#acacac"));
//                        break;
                    case R.id.back:
                        //删除上一个输入的内容，就显示input中第一个到倒数第二个字符即可，同时重新赋值input
//                        input = input.substring(0,input.length()-1);
                        if(input.length()>1){
                            input = input.substring(0,input.length()-1);
                            binding.top.setText(input);
                            Log.d("ljkljk20",inputChar.get(inputChar.size()-1));
                            inputChar.remove(inputChar.size()-1);
                            nibo.clear();
                            tran();

                        //删除输入之后，字体，颜色再变回来，重新点击等号再重新改变
                        binding.top.setTextSize(50);
                        binding.result.setTextSize(40);
                        binding.top.setTextColor(Color.parseColor("#000000"));
                        binding.result.setTextColor(Color.parseColor("#acacac"));
//                        binding.result.setText("");//只输入数字，结果等于本身
//                        binding.result.append("="+res);
                }
                        else{
                            binding.top.setText("");
                            //输出置为初始状态0
                            binding.result.setText("0");
                            //清空接收输入的input和显示学号姓名的input1
                            input = "";
                            input1 = "";
                            //str回到初值
                            str = 0;
                        }
                        break;
                    //清空当前所有内容
                    case R.id.clear:
                        //清除显示
                        inputChar.clear();
                        binding.top.setText("");
                        //输出置为初始状态0
                        binding.result.setText("0");
                        //清空接收输入的input和显示学号姓名的input1
                        input = "";
                        input1 = "";
                        //str回到初值
                        str = 0;
                        //字体颜色恢复
                        binding.top.setTextSize(50);
                        binding.result.setTextSize(40);
                        binding.top.setTextColor(Color.parseColor("#000000"));
                        binding.result.setTextColor(Color.parseColor("#acacac"));
                        break;
                    case R.id.s_deng:
                        nibo.clear();
                        tran();
                        count();
                        try {
                            jisuan();
                        } catch (Exception e){
                            Toast.makeText(MainActivity.this,"输入有误！",Toast.LENGTH_LONG).show();
                        }
//                        if (ops.size()==1){binding.result.setText(String.valueOf(ops.pop()));}
                        if (ops.size()==1){
                            String res = String.valueOf(ops.pop());
                            String res_l = res.substring(0,res.indexOf("."));
                            String res_r = res.substring(res.indexOf(".")+1,res.length());
                            if("0".equals(res_r)){
                                binding.result.setText(res_l);
                                }
                            else binding.result.setText(res);
                        }
//                        //对于结算结果点击等号的时候，上下文本框交换颜色与字体大小

                            binding.top.setTextSize(40);
                            binding.result.setTextSize(50);
                            binding.top.setTextColor(Color.parseColor("#acacac"));
                            binding.result.setTextColor(Color.parseColor("#000000"));//不可以简写“000” 程序会闪退
                        break;
                }
            }
        };
        //加入所有的id
        binding.s0.setOnClickListener(onClickListener);
        binding.s1.setOnClickListener(onClickListener);
        binding.s2.setOnClickListener(onClickListener);
        binding.s3.setOnClickListener(onClickListener);
        binding.s4.setOnClickListener(onClickListener);
        binding.s5.setOnClickListener(onClickListener);
        binding.s6.setOnClickListener(onClickListener);
        binding.s7.setOnClickListener(onClickListener);
        binding.s8.setOnClickListener(onClickListener);
        binding.s9.setOnClickListener(onClickListener);
        binding.sDian.setOnClickListener(onClickListener);
        binding.sA.setOnClickListener(onClickListener);
        binding.sJ.setOnClickListener(onClickListener);
        binding.sCheng.setOnClickListener(onClickListener);
        binding.sChu.setOnClickListener(onClickListener);
        binding.sDeng.setOnClickListener(onClickListener);
        binding.back.setOnClickListener(onClickListener);
        binding.clear.setOnClickListener(onClickListener);
        binding.top.setOnClickListener(onClickListener);
        binding.result.setOnClickListener(onClickListener);
        binding.sE.setOnClickListener(onClickListener);
        binding.sLog.setOnClickListener(onClickListener);
        binding.sTan.setOnClickListener(onClickListener);
        binding.sCos.setOnClickListener(onClickListener);
        binding.sSin.setOnClickListener(onClickListener);
        binding.sMi.setOnClickListener(onClickListener);
        binding.sJiecheng.setOnClickListener(onClickListener);
        binding.sSqrt.setOnClickListener(onClickListener);
        binding.sLeftc.setOnClickListener(onClickListener);
        binding.sRightc.setOnClickListener(onClickListener);
    }
}
