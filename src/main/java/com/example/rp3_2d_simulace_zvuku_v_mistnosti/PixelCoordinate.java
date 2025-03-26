package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class PixelCoordinate {
    private final int x;
    private final int y;

    public PixelCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PixelCoordinate that = (PixelCoordinate) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }


}
