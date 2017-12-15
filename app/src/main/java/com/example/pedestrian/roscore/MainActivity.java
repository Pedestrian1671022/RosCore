package com.example.pedestrian.roscore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ros.RosCore;
import org.ros.android.RosActivity;
import org.ros.message.MessageListener;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class MainActivity extends RosActivity {
    static
    {
        RosCore rosCore = RosCore.newPublic("202.118.26.131", 11311);
        rosCore.start();
    }

    private TextView textView;
    private Button button;

    private Talker talker;
    private Listener listener;

    public MainActivity() {
        super("RosCore", "RosCore");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        listener = new Listener("/text", std_msgs.String._TYPE, new MessageListener<std_msgs.String>() {
            @Override
            public void onNewMessage(final std_msgs.String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(message.getData());
                    }
                });

            }
        });

        talker = new Talker("/text", std_msgs.String._TYPE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                talker.publish("liuxinshisssh");
            }
        });
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(talker, nodeConfiguration);
        nodeMainExecutor.execute(listener, nodeConfiguration);
    }
}
