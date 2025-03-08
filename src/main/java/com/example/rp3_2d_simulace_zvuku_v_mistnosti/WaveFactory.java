package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class WaveFactory {
    /**
     * Creates a new SoundWave at the given position.
     *
     * @param x         The starting X coordinate of the wave.
     * @param y         The starting Y coordinate of the wave.
     * @return A new instance of SoundWave.
     */
    public SoundWave createWave(double x, double y, BaseRoomControllerInterface controller, int radius,int okamzitaVychylka, int amplitude, int direction) {
        // Instantiate a new SoundWave object with the provided parameters



        if (okamzitaVychylka > 0) {
            if (amplitude <= okamzitaVychylka) {
                okamzitaVychylka = amplitude;
            }
        }else if (okamzitaVychylka < 0){
            if (-amplitude >= okamzitaVychylka) {
                okamzitaVychylka = -amplitude;
            }
        }
/*
        // Print the okamzita vychylka, amplitude, radius, and direction
        System.out.println("okamzitaVychylka: " + okamzitaVychylka);
        System.out.println("amplitude: " + amplitude);
        System.out.println("radius: " + radius);
        System.out.println("direction: " + direction);
        System.out.println("---------------");
*/
        return new SoundWave(x, y, controller, radius, okamzitaVychylka, amplitude, direction);
    }


}
