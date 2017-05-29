package com.example.anthonsteiness.handyjuralayout.data;

import com.example.anthonsteiness.handyjuralayout.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Simon_ on 18-05-2017.
 */

public class TaskData {


    public List<Task> getTasks(){
      return Arrays.asList(
              new Task("test"),
              new Task("test"),
              new Task("test"),
              new Task("test"),
        new Task("test")
      );
    }



}
