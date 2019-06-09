package wp.app.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 56864 on 2017/5/28.
 */

public class ExamActivity extends Activity {
    private int count;
    private int current;
    private TextView tv_question,tv_explaination,question_id;
    private Button btn_previous,btn_next;
    private RadioGroup radioGroup;
    private boolean wrongMode;
    private boolean radio_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        init();
    }
    public void init(){
        DBService dbService = new DBService();
        final List<Question> list = dbService.getQuestion();
        count = list.size();
        current = 0;
        wrongMode = false;
        radio_status = false;
        tv_question = (TextView)findViewById(R.id.question);
        tv_explaination = (TextView)findViewById(R.id.explaination);
        question_id = (TextView)findViewById(R.id.question_id);
        final RadioButton[] radioButtons = new RadioButton[4];
        radioButtons[0] = (RadioButton)findViewById(R.id.answera);
        radioButtons[1] = (RadioButton)findViewById(R.id.answerb);
        radioButtons[2] = (RadioButton)findViewById(R.id.answerc);
        radioButtons[3] = (RadioButton)findViewById(R.id.answerd);
        btn_previous = (Button)findViewById(R.id.btn_previous);
        btn_next = (Button)findViewById(R.id.btn_next);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        Question q = list.get(0);
        tv_question.setText(q.question);
        tv_explaination.setText(q.explaination);
//        question_id.setText("（"+q.ID+"）"+".");
//        question_id.setText(q.ID+".");
        question_id.setText("NO"+"."+q.ID);
        radioButtons[0].setText(q.answerA);
        radioButtons[1].setText(q.answerB);
        radioButtons[2].setText(q.answerC);
        radioButtons[3].setText(q.answerD);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (current < count -1){
                            for(int i=0;i<radioButtons.length;i++) {
                                if (radioButtons[i].isChecked() == true) {
                                    current++;
                                    Question q = list.get(current);
                                    tv_question.setText(q.question);
//                                    question_id.setText(q.ID + ".");
                                    question_id.setText("NO"+"."+q.ID);
                                    radioButtons[0].setText(q.answerA);
                                    radioButtons[1].setText(q.answerB);
                                    radioButtons[2].setText(q.answerC);
                                    radioButtons[3].setText(q.answerD);
                                    tv_explaination.setText(q.explaination);
                                    radioGroup.clearCheck();
                                    if (q.selectedAnswer != -1) {
                                        radioButtons[q.selectedAnswer].setChecked(true);
                                    }
                                } else {
//                                  Toast.makeText(ExamActivity.this,"请答题",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else if(current == count -1 && wrongMode == true){
                                new AlertDialog.Builder(ExamActivity.this)
                                        .setTitle("提示")
                                        .setMessage("已经到达最后一题，是否退出？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ExamActivity.this.finish();
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .show();
                        } else {
                            final List<Integer> wronglist = checkAnswer(list);
                            if(wronglist.size() == 0){
                                new AlertDialog.Builder(ExamActivity.this)
                                        .setTitle("提示")
                                        .setMessage("恭喜你全部回答正确！")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ExamActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }else {
                                new AlertDialog.Builder(ExamActivity.this)
                                        .setTitle("提示")
                                        .setMessage("您答对了"+(list.size() - wronglist.size()) +
                                                "道题目，答错了"+wronglist.size()+"道题目。是否查看错题？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                wrongMode = true;
                                                List<Question>  newList = new ArrayList<Question>();
                                                for(int i=0;i<wronglist.size();i++){
                                                    newList.add(list.get(wronglist.get(i)));
                                                }
                                                list.clear();
                                                for (int i=0;i<newList.size();i++){
                                                    list.add(newList.get(i));
                                                }
                                                current = 0;
                                                count = list.size();
                                                Question  q = list.get(current);
                                                tv_question.setText(q.question);
//                                                question_id.setText(q.ID+".");
                                                question_id.setText("NO"+"."+q.ID);
                                                radioButtons[0].setText(q.answerA);
                                                radioButtons[1].setText(q.answerB);
                                                radioButtons[2].setText(q.answerC);
                                                radioButtons[3].setText(q.answerD);
                                                radioButtons[q.selectedAnswer].setChecked(true);
                                                tv_explaination.setText(q.explaination);
                                                tv_explaination.setVisibility(View.VISIBLE);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ExamActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }
                }

            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current > 0) {
                    current--;
                    Question q = list.get(current);
                    tv_question.setText(q.question);
//                    question_id.setText(q.ID+".");
                    question_id.setText("NO"+"."+q.ID);
                    radioButtons[0].setText(q.answerA);
                    radioButtons[1].setText(q.answerB);
                    radioButtons[2].setText(q.answerC);
                    radioButtons[3].setText(q.answerD);
                    tv_explaination.setText(q.explaination);
                    radioGroup.clearCheck();
                    if (q.selectedAnswer != -1) {
                        radioButtons[q.selectedAnswer].setChecked(true);
                    }
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<4;i++){
                    if(radioButtons[i].isChecked() == true){
                          list.get(current).selectedAnswer = i;
                          radio_status = true;
                          break;
                    }
                }
            }
        });
    }

    /**
     * 用户作答是否正确的方法
     * 并且能够获取用户作答错误的列表
     * @param list
     * @return
     */
    private List<Integer> checkAnswer(List<Question> list){

        List<Integer> wrongList = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            if(list.get(i).answer != list.get(i).selectedAnswer){
                wrongList.add(i);
            }
        }
        return wrongList;
    }
}
