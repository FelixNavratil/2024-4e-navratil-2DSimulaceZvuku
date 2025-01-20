package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class WaveFactory {
    /**
     * Creates a new SoundWave at the given position.
     *
     * @param x         The starting X coordinate of the wave.
     * @param y         The starting Y coordinate of the wave.
     * @param maxRadius The maximum radius the wave can grow to.
     * @return A new instance of SoundWave.
     */
    public SoundWave createWave(double x, double y, double maxRadius) {
        // Instantiate a new SoundWave object with the provided parameters
        return new SoundWave(x, y);
    }

    /**
     * (Optional Method) Creates a different type of wave if needed in the future.
     * Could be extended to handle multiple wave types dynamically.
     *
     * @param type      The type of wave to create (e.g., "sound", "visual").
     * @param x         The starting X coordinate of the wave.
     * @param y         The starting Y coordinate of the wave.
     * @return A new instance of the specified wave type.
     */
    public SoundWave createWave(String type, double x, double y) {
        // Example: Add logic for different wave types if needed
        if ("sound".equalsIgnoreCase(type)) {
            return new SoundWave(x, y);
        } else {
            // Placeholder for other types of waves
            throw new UnsupportedOperationException("Wave type not supported: " + type);
        }
    }
}
