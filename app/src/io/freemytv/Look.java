package io.freemytv;

/**
 * This fork's curated palettes (Settings > Palette and Settings > Accent). The stock look keeps its sun-following
 * aurora (palette 0); each other palette is a solid background with flat solid panels, in Scene.setMinimal. Ink stays
 * white everywhere, as in the stock look: every palette's panels are dark, and the light palettes' backgrounds sit
 * around the luminance of the stock day sky, which the stock look also keeps readable with white ink.
 */
final class Look {
    private Look() {}

    /** Settings rows: palette 0 keeps the stock aurora. */
    static final String[] NAMES = {"Aurora", "Grafite", "Meia-noite", "Floresta", "Ameixa", "Papel", "Osso"};
    /** Per palette: background, panel, panel bright (focus), accent. The dark ones differ clearly in hue:
     *  slate, navy, green, plum; Papel and Osso are the light-walled ones, rendered as day (dark ink on light). */
    private static final int[][] PALETTES = {
            {0x181A1F, 0x232630, 0x2E3240, 0xE8B54B},   // Grafite: slate charcoal, the neutral one
            {0x0A1030, 0x141E50, 0x1E2A6E, 0x6CB4F0},   // Meia-noite: deep navy
            {0x0A2416, 0x123A22, 0x1A5030, 0x7ED9A3},   // Floresta: deep green
            {0x241030, 0x3C1E50, 0x50286A, 0xC9A0D8},   // Ameixa: plum purple
            {0xECEDEB, 0xF7F8F6, 0xFFFFFF, 0xC2410C},   // Papel: light wall, light panels, burnt-orange accent
            {0xEAE3D3, 0xF5F0E6, 0xFFFDF6, 0x8A5A19},   // Osso: warm bone, bronze accent
    };

    /** The light-walled palettes: the home renders as day, with the app's dark day ink over them. */
    static boolean light() {
        int p = palette();
        return p == 5 || p == 6;
    }

    /** Text ink for the light palettes (the others keep the app's own, white at night). */
    static int ink() {
        return light() ? 0xFF1D1D1F : 0xFFFFFFFF;
    }

    /** `stock` where the light palettes want their dark ink instead. */
    static int ink(int stock) {
        return light() ? 0xFF1D1D1F : 0xFF000000 | (stock & 0xFFFFFF);
    }

    /** Accent swatches: 0 keeps the palette's own. */
    static final String[] ACCENTS = {"Paleta", "Âmbar", "Oceano", "Menta", "Violeta", "Coral", "Ouro", "Ciano", "Branco"};
    private static final int[] SWATCHES = {0, 0xE8B54B, 0x5B9BD5, 0x6FBF8B, 0xB78BC4, 0xE36A5C, 0xD9B54A, 0x51B8C4,
            0xF2F3F5};

    static int palette() {
        return Math.max(0, Math.min(PALETTES.length, Prefs.look()));
    }

    /** True when a solid palette is picked (the stock aurora is palette 0). */
    static boolean solid() {
        return palette() != 0;
    }

    private static int[] p() {
        // Palette 0 (the stock aurora) never asks for these, but the callers pass them whatever the palette:
        // its colors come out as Grafite's, and Scene ignores them while solid() is false.
        return PALETTES[Math.max(0, palette() - 1)];
    }

    static int background() {
        return 0xFF000000 | p()[0];
    }

    /** The flat panels' color (Scene.glass and Scene.liquid in minimal mode). */
    static int panel() {
        return 0xFF000000 | p()[1];
    }

    /** Panels a little brighter: what a glass with a light tint (the focused) becomes in minimal mode. */
    static int panelHi() {
        return 0xFF000000 | p()[2];
    }

    static int accent() {
        int swatch = Prefs.accent();
        return 0xFF000000 | (swatch > 0 && swatch < SWATCHES.length ? SWATCHES[swatch] : p()[3]);
    }

    /**
     * The accent where the stock look uses `stock` (white focus rims and highlights): the picked accent when a solid
     * palette is on, `stock` with the stock aurora.
     */
    static int accent(int stock) {
        return solid() ? accent() : 0xFF000000 | (stock & 0xFFFFFF);
    }
}
