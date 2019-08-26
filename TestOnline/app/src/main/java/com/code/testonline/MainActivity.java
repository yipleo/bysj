package com.code.testonline;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.code.testonline.bean.QuestionBean;
import com.code.testonline.utils.FileUtils;
import com.code.testonline.utils.HttpDownloader;
import com.code.testonline.utils.XmlParserUtils;

import java.io.File;
import java.util.List;

/**
 * 答题界面
 */
public class MainActivity extends BaseActivity {

    private final String url = "http://120.78.161.229/test01.xml"; //url
    private final String savePath = File.separator + "TestOnline" + File.separator + "questions"; //保存路径
    private final String filename = "test.xml"; //保存的文件名

    private List<QuestionBean> questionBeanList; //问题列表
    private int index; //标记答到第几道题
    private String selectAnswer = ""; //选择的选项

    private Button btnDownload;

    private ScrollView svQuestion;
    private TextView tvQuestion;
    private RadioGroup rgChoices;
    private RadioButton rbChoice1;
    private RadioButton rbChoice2;
    private RadioButton rbChoice3;
    private RadioButton rbChoice4;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定控件id
        btnDownload = findViewById(R.id.btn_download_test);
        svQuestion = findViewById(R.id.sv_question);
        tvQuestion = findViewById(R.id.tv_question);
        rgChoices = findViewById(R.id.rg_choices);
        rbChoice1 = findViewById(R.id.rb_choice1);
        rbChoice2 = findViewById(R.id.rb_choice2);
        rbChoice3 = findViewById(R.id.rb_choice3);
        rbChoice4 = findViewById(R.id.rb_choice4);
        btnSubmit = findViewById(R.id.btn_submit);


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击下载按钮
                downloadXmlFile();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击提交按钮
                judgeAnswer();
            }
        });

        rgChoices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) { //选择答案
                switch (checkedId) { //根据答案记录选了哪个
                    case R.id.rb_choice1:
                        selectAnswer = "A";
                        break;
                    case R.id.rb_choice2:
                        selectAnswer = "B";
                        break;
                    case R.id.rb_choice3:
                        selectAnswer = "C";
                        break;
                    case R.id.rb_choice4:
                        selectAnswer = "D";
                        break;

                }
            }
        });
    }

    /**
     * 在UI线程，处理从子线程传过来的数据
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: //已下载过
                case 0: //下载成功
                    questionBeanList = XmlParserUtils.parser(FileUtils.getDiscFileDir(MainActivity.this)
                            + savePath + File.separator + filename); //解析xml
                    if (questionBeanList == null || questionBeanList.isEmpty()) { //解析好的题目集合
                        svQuestion.setVisibility(View.GONE); //隐藏答题界面
                        Toast.makeText(MainActivity.this, "解析题目失败", Toast.LENGTH_SHORT).show();
                    } else {
                        svQuestion.setVisibility(View.VISIBLE); //显示答题界面
                        switchQuestion(questionBeanList, 1); //从第一题开始
                    }
                    break;
                case -1:
                    Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 下载xml文件
     * <p>
     * 网络操作需要放在子线程执行
     */
    private void downloadXmlFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = HttpDownloader.downloadFiles(MainActivity.this, url, savePath, filename);
                //将结果传到UI线程
                Message message = handler.obtainMessage();
                message.what = result;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 切换题目
     *
     * @param list  所有题目
     * @param index 切换哪个题
     */
    private void switchQuestion(List<QuestionBean> list, int index) {
        if (index > list.size()) {
            return;
        }

        rbChoice1.setChecked(true); //每次切换题目，默认选中A
        this.index = index;
        QuestionBean questionBean = list.get(--index); //拿到题目
        if (questionBean != null && questionBean.choices != null) {
            tvQuestion.setText((index + 1) + "、" + questionBean.description); //题目
            //选项
            rbChoice1.setText("A、 " + questionBean.choices.get(0));
            rbChoice2.setText("B、 " + questionBean.choices.get(1));
            rbChoice3.setText("C、 " + questionBean.choices.get(2));
            rbChoice4.setText("D、 " + questionBean.choices.get(3));
        }
    }

    /**
     * 判断答案
     */
    private void judgeAnswer(){
        if (questionBeanList != null && !questionBeanList.isEmpty()) {

            if (index <= questionBeanList.size()) { //是否答到最后一题

                String answerTrue = questionBeanList.get(index - 1).answer;
                if (selectAnswer.equals(answerTrue)) { //判断答案
                    Toast.makeText(MainActivity.this, "回答正确", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "回答错误：正确答案为" + answerTrue, Toast.LENGTH_SHORT).show();
                }

                switchQuestion(questionBeanList, ++index); //切换下一道题

            } else {
                Toast.makeText(MainActivity.this, "答题结束", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "暂无题目", Toast.LENGTH_SHORT).show();
        }
    }
}
