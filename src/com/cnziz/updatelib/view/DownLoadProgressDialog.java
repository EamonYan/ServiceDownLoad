
/**************************************************************************************
* [Project]
*       MyProgressDialog
* [Package]
*       com.lxd.widgets
* [FileName]
*       CustomProgressDialog.java
* [Copyright]
*       Copyright 2012 LXD All Rights Reserved.
* [History]
*       Version          Date              Author                        Record
*--------------------------------------------------------------------------------------
*       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
**************************************************************************************/
	
package com.cnziz.updatelib.view;



import com.cnziz.updatelib.R;
import com.cnziz.updatelib.R.id;
import com.cnziz.updatelib.R.layout;
import com.cnziz.updatelib.R.style;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/********************************************************************
 * [Summary]
 *       TODO ���ڴ˴���Ҫ����������ʵ�ֵĹ��ܡ���Ϊ����ע����Ҫ��Ϊ����IDE����������tip��������ؼ�����Ҫ
 * [Remarks]
 *       TODO ���ڴ˴���ϸ������Ĺ��ܡ����÷�����ע������Լ���������Ĺ�ϵ.
 *******************************************************************/

public class DownLoadProgressDialog extends Dialog {
	private Context context = null;
	private TextView progressText;
	private ImageView imageView;
	private View contentView;
	
	public DownLoadProgressDialog(Context context){
		super(context, R.style.CustomProgressDialog);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(context).inflate(R.layout.load_file_progress_dialog_layout, null);
		setContentView(contentView);
		setCanceledOnTouchOutside(false);
		progressText = (TextView) contentView.findViewById(R.id.progress);
		getWindow().getAttributes().gravity = Gravity.CENTER;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
        ImageView imageView = (ImageView) contentView.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
    
    public void setProgress(int progress){
    	progressText.setText(progress+"%");
    }
    
    /**
     * 
     * [Summary]
     *       setMessage ��ʾ����
     * @param strMessage
     * @return
     *
     */
    public void setMessage(String strMessage){
    	TextView tvMsg = (TextView)contentView.findViewById(R.id.id_tv_loadingmsg);
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    }
}
