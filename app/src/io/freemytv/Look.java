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
    /** Per palette: background, panel, panel bright (focus), accent. */
    private static final int[][] PALETTES = {
            {0x111318, 0x1C1F26, 0x272B33, 0xE8B54B},   // Grafite (dark)
            {0x0C111A, 0x161D2A, 0x212B3C, 0x5B9BD5},   // Meia-noite (dark)
            {0x0E1511, 0x18221B, 0x232F26, 0x6FBF8B},   // Floresta (dark)
            {0x161018, 0x221A26, 0x2E2432, 0xB78BC4},   // Ameixa (dark)
            {0xAEB0AC, 0x1E2023, 0x2A2D31, 0xE8B54B},   // Papel (light wall, dark panels)
            {0xB2A899, 0x232019, 0x302B22, 0xD9B54A},   // Osso (light wall, dark panels)
    };

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
        return PALETTES[palette() - 1];
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
