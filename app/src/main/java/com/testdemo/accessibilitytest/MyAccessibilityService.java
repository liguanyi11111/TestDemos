package com.testdemo.accessibilitytest;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by liguanyi on 15-11-28.
 */
public class MyAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String eventText = "";
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventText = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventText = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventText = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventText = "TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventText = "TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventText = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
        }
        Log.i("lgy", "---------------->eventText " + eventText + " pkg: " + event.getPackageName());
        onAction(event);
    }

    private void onAction(AccessibilityEvent event){
        String targetPackageName = "com.miui.securitycenter";
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            if(event.getPackageName().equals(targetPackageName)){
                AccessibilityNodeInfo info = getRootInActiveWindow();
                print(info);
            }
        }
    }

    public void print(AccessibilityNodeInfo info) {
        if(info == null){
            return;
        }
        String parent = printParent(info);
        String child = printChild(0, info);

        Log.d("lgy", "parent: " + parent + "child: " + child);
    }

    public String printParent(AccessibilityNodeInfo info){
        String parent = "";
        while (info != null){
            parent = info.getClassName() + "\n" + parent;
            info = info.getParent();
        }
        return parent;
    }

    public String printChild(int x, AccessibilityNodeInfo info){
        String child = "";
        if(info.getChildCount() > 0) {
            String spaces = "";
            for(int i = 0 ; i < x ; i++){
                spaces += " ";
            }
            x += 4;
            child =  child + "{";
            for (int i = 0; i < info.getChildCount(); i++) {
                AccessibilityNodeInfo childNode = info.getChild(i);
                child = child + "\n" + spaces + childNode.getClassName() +
                        " text: " +  childNode.getText() +
                        " id: " + childNode.getViewIdResourceName()
                        + printChild(x, childNode);
            }
            child = child + "\n" + spaces + "}";
        }
        return child;
    }


    @Override
    public void onInterrupt() {

    }
}
