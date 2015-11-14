package com.delbridge.microgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Manages all the games states - menu, play, pause, gameover, etc.
 * Represented by a stack because states naturally stack on top of eachother:
 *      2. Pause Menu
 *      1. Play State
 *
 *  There will usually only be one state anyways unless the player pauses the game or
 *  a gameover state is pushed to the stack
 *
 *  Basic stack functionality: push, pop, peek
 *  Set state swaps the top state with a different one
 *
 *
 */
public class GameStateManager {
    private Stack<State> states;
    public static final int MENU = 0;
    public static final int PLAY = 1;
    public int currentState;
    public GameStateManager(){
        states = new Stack<>();
    }

    public void pop(){
        states.pop();
    }

    public void push(State s){
        states.push(s);
    }

    public State peek(){
        return states.peek();
    }

    public void setState(State s){
        states.pop();
        states.push(s);
    }

    public void update(float dt){
        updateStates(dt);
    }

    private void updateStates(float dt){
        if(states.size() > 1){
            State top = states.pop();
            updateStates(dt);
            states.push(top);
        }
        states.peek().update(dt);
    }

    //Recursively draws all the states by recurring to the bottom of the
    //stack and calling state render methods on the way up
    public void render(SpriteBatch sb){
        if(states.size() > 1){
            State top = states.pop();
            render(sb);
            states.push(top);
        }
        //Render state
        states.peek().render(sb);
    }
}
