package com.example.move4music;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

public class SoundManager {
	public static int SOUNDPOOLSND_MENU_BTN = 0;
	public static int SOUNDPOOLSND_WIN = 1;
	public static int SOUNDPOOLSND_LOOSE = 2;
	public static int SOUNDPOOLSND_DRAW = 3;
	public static int SOUNDPOOLSND_TICK1 = 4;
	public static int SOUNDPOOLSND_TICK2 = 5;
	public static int SOUNDPOOLSND_OUT_OF_TIME = 6;
	public static int SOUNDPOOLSND_HISCORE = 7;
	public static int SOUNDPOOLSND_CORRECT_LETTER = 8;

	public static boolean isSoundTurnedOff;

	private static SoundManager mSoundManager;

	private SoundPool mSoundPool;
	private SparseArray<Integer> mSoundPoolMap;
	private SparseArray<Integer> mPausePoolMap;
	private AudioManager mAudioManager;

	public static final int maxSounds = 4;

	public static SoundManager getInstance(Context context) {
		if (mSoundManager == null) {
			mSoundManager = new SoundManager(context);
		}

		return mSoundManager;
	}

	public SoundManager(Context mContext) {
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mSoundPool = new SoundPool(maxSounds, AudioManager.STREAM_MUSIC, 0);

		mPausePoolMap = new SparseArray<Integer>();
		mSoundPoolMap = new SparseArray<Integer>();
		mSoundPoolMap.put(SOUNDPOOLSND_MENU_BTN, mSoundPool.load(mContext, R.raw.viva_la_vida, 1));
		mSoundPoolMap.put(SOUNDPOOLSND_WIN, mSoundPool.load(mContext, R.raw.piano, 1));

		// testing simultaneous playing
//		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//		mSoundPool.play(mSoundPoolMap.get(0), streamVolume, streamVolume, 1, 20, 1f);
//		mSoundPool.play(mSoundPoolMap.get(1), streamVolume, streamVolume, 1, 2, 1f);
	}

	public void playSound(int index) {
		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int idS = mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
		mPausePoolMap.put(index, idS);
	}

	public void pauseSound(int index) {
		mSoundPool.pause(mPausePoolMap.get(index));
	}
	
	public static void clear() {
		if (mSoundManager != null) {
			mSoundManager.mSoundPool = null;
			mSoundManager.mAudioManager = null;
			mSoundManager.mSoundPoolMap = null;
		}
		mSoundManager = null;
	}
	
	public void mudaVelocidade(int index, float y) {
		float velocidade = (Math.abs(y)/10000f) + 0.5f;
		mSoundPool.setRate(mPausePoolMap.get(index), velocidade);
	}
}
