package dev.emmaguy.fruitninja;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;

public class GameThread implements Runnable {

    private final SurfaceHolder surfaceHolder;
    private final GameTimer timer = new GameTimer();
    private final OnGameOver gameOverListener;
    private final ProjectileManager projectileManager;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private boolean isRunning = false;
    private volatile ScheduledFuture<?> self;
    private final Paint scorePaint = new Paint();
    private int score = 0;
    private int width = 0;
    
    public GameThread(SurfaceHolder surfaceHolder, ProjectileManager projectileManager, OnGameOver gameOverListener) {
	this.surfaceHolder = surfaceHolder;
	this.projectileManager = projectileManager;
	this.gameOverListener = gameOverListener;
    }

    public void pauseGame() {
	isRunning = false;
	timer.pauseGame();
    }

    public void resumeGame(int width, int height) {
	this.width = width;
	isRunning = true;
	timer.resumeGame();
	projectileManager.setWidthAndHeight(width, height);
    }

    public void startGame(int width, int height) {
	this.width = width;
	isRunning = true;
	projectileManager.setWidthAndHeight(width, height);
	timer.startGame();
	self = executor.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
	
	scorePaint.setColor(Color.MAGENTA);
	scorePaint.setAntiAlias(true);
	scorePaint.setTextSize(38.0f);
    }

    @Override
    public void run() {
	Canvas canvas = null;
	if (isRunning) {
	    try {

		if (timer.isGameFinished()) {
		    isRunning = false;
		    gameOverListener.onGameOver(score);
		    self.cancel(true);
		} else {

		    projectileManager.update();

		    canvas = surfaceHolder.lockCanvas();
		    if (canvas != null) {
			synchronized (surfaceHolder) {
			    canvas.drawARGB(255, 0, 0, 0);

			    projectileManager.draw(canvas);
			    timer.draw(canvas);
			    canvas.drawText("Score: " + score, width - 160, 50, scorePaint);
			}
		    }
		}
	    } finally {
		if (canvas != null) {
		    surfaceHolder.unlockCanvasAndPost(canvas);
		}
	    }
	}

    }

    public void incrementScore() {
	this.score++;
    }
}