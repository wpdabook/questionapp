package wp.app.com;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 56864 on 2017/5/28.
 * 该类用来连接数据库，并从数据库中获取所需要数据
 */

public class DBService {
    private SQLiteDatabase db;
    public DBService(){
        /**
         * 打开指定数据库并把引用指向DB
         */
        db  = SQLiteDatabase.openDatabase("/data/data/wp.app.com.five/databases/question.db"
                ,null,SQLiteDatabase.OPEN_READWRITE);
    }
    /**
     * 获取数据库中的问题b
     */
    public List<Question> getQuestion(){
        List<Question> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from question",null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            int count = cursor.getCount();
            for(int i=0;i<count;i++){
                cursor.moveToPosition(i);
                Question question = new Question();
                question.ID = cursor.getInt(cursor.getColumnIndex("ID"));
                question.question = cursor.getString(cursor.getColumnIndex("question"));
                question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                question.ID = cursor.getInt(cursor.getColumnIndex("ID"));
                question.explaination = cursor.getString(cursor.getColumnIndex("explaination"));
                question.selectedAnswer = -1;
                list.add(question);
            }
        }
        return list;
    }


}
