/**
* The random number generator from POWDER, originally designed by Jeff Lait.
*
*<p>Like most RNG code, this entire class operates on pixie dust and crystal meth, so I'd strongly advise against messing with anything here.</p>
*
*<p>Realistically, you can get away with using Java's Random class with no noticeable issue, this is just to maintain 1:1 parity with POWDER. However, you're gonna want to copy rand_dirtoangle, rand_angletodir, rand_getDirection, and the rand_shuffle functions if you decide to skip including this class</p>
*/
public class rand
{
    static mt19937 randomizer = new mt19937();;
    public static byte[] percentTable = rand_print();
    static final int[][] glb_dpdftable = new int[][] {
    	{ 384, 512, 640, 768, 896, 1024, 1152, 1280, 1408, 1536, 1664, 1792, 1920, 2048, 2176, 2304, 2432, 2560, 2688, 2816, 2944, 3072, 3200, 3328, 3456, 3584, 3712, 3840, 3968, 4096, 4224, 4352, 4480, 4608, 4736, 4864, 4992, 5120, 5248, 5376, 5504, 5632, 5760, 5888, 6016, 6144, 6272, 6400, 6528 },
    	{ 448, 626, 800, 973, 1145, 1317, 1488, 1659, 1830, 2001, 2172, 2343, 2514, 2685, 2856, 3027, 3198, 3368, 3539, 3710, 3881, 4051, 4222, 4393, 4564, 4734, 4905, 5076, 5247, 5417, 5588, 5759, 5929, 6100, 6271, 6442, 6612, 6783, 6954, 7124, 7295, 7466, 7636, 7807, 7978, 8148, 8319, 8490, 8660 },
    	{ 480, 683, 880, 1075, 1269, 1463, 1656, 1849, 2042, 2234, 2427, 2619, 2811, 3004, 3196, 3388, 3580, 3773, 3965, 4157, 4349, 4541, 4733, 4925, 5118, 5310, 5502, 5694, 5886, 6078, 6270, 6462, 6654, 6846, 7038, 7230, 7422, 7614, 7806, 7998, 8190, 8383, 8575, 8767, 8959, 9151, 9343, 9535, 9727 },
    	{ 496, 714, 926, 1135, 1343, 1549, 1756, 1962, 2167, 2373, 2578, 2784, 2989, 3194, 3399, 3605, 3810, 4015, 4220, 4425, 4630, 4835, 5040, 5245, 5450, 5654, 5859, 6064, 6269, 6474, 6679, 6884, 7089, 7294, 7498, 7703, 7908, 8113, 8318, 8523, 8728, 8932, 9137, 9342, 9547, 9752, 9957, 10161, 10366 },
    	{ 504, 733, 955, 1174, 1390, 1606, 1821, 2036, 2251, 2465, 2679, 2893, 3107, 3321, 3535, 3748, 3962, 4176, 4389, 4603, 4816, 5030, 5244, 5457, 5671, 5884, 6098, 6311, 6524, 6738, 6951, 7165, 7378, 7592, 7805, 8018, 8232, 8445, 8659, 8872, 9085, 9299, 9512, 9726, 9939, 10152, 10366, 10579, 10793 },
    	{ 508, 745, 974, 1200, 1423, 1646, 1868, 2089, 2310, 2530, 2751, 2971, 3191, 3411, 3631, 3851, 4071, 4290, 4510, 4730, 4950, 5169, 5389, 5609, 5828, 6048, 6267, 6487, 6707, 6926, 7146, 7365, 7585, 7804, 8024, 8243, 8463, 8682, 8902, 9121, 9341, 9560, 9780, 9999, 10219, 10438, 10658, 10877, 11097 }
    };
    
    public static int DPDF_MAXSIDES = 50;
    public static int DPDF_MAXROLLS = 5;
    public static int DPDF_SCALE = 256;
    
    static byte[] rand_print() {
        final byte[] table = new byte[1024];
        for (int i = 0; i <= 1023; ++i) {
            float val = i / 1024.0f;
            val *= 100.0f;
            table[i] = (byte)val;
        }
        return table;
    }
    
    public static int getrandom() {
        return rand.randomizer.nextInt();
    }
    
    public static long rand_getseed() {
        final long seed = rand.randomizer.nextLong();
        rand_setseed(seed);
        return seed;
    }
    
    public static void rand_setseed(final long seed) {
        rand.randomizer.setSeed(seed);
    }
    
    public static double rand_double() {
        return rand.randomizer.nextDouble();
    }
    
    public static int rand_range(final int min, final int max) {
        if (min > max) {
            return rand_range(max, min);
        }
        final int v = rand_choice(max - min + 1);
        return v + min;
    }
    
    public static int rand_choice(final int num) {
        if (num < 2) {
            return 0;
        }
        switch (num) {
            case 2: {
                return rand.randomizer.nextInt() & 0x1;
            }
            case 3: {
                int fullrandom;
                for (fullrandom = rand.randomizer.nextInt(); (fullrandom & 0x3) == 0x3; fullrandom >>= 2) {}
                return fullrandom & 0x3;
            }
            case 4: {
                return rand.randomizer.nextInt() & 0x3;
            }
            case 8: {
                return rand.randomizer.nextInt() & 0x7;
            }
            case 100: {
                final int index = rand.randomizer.nextInt() & 0x3FF;
                return rand.percentTable[index];
            }
            default: {
                int v = Math.abs(rand.randomizer.nextInt());
                v %= num;
                return v;
            }
        }
    }
    
    public static String rand_string(final String[] stringlist) {
        final int n = stringlist.length;
        return stringlist[rand_choice(n)];
    }
    
    public static int rand_roll(final int num, int reroll) {
        int max = 0;
        if (num >= -1 && num <= 1) {
            return num;
        }
        if (num < 0) {
            int val = rand_roll(-num, reroll);
            val -= num + 1;
            return val;
        }
        if (reroll < 0) {
            int val = rand_roll(num, -reroll);
            val = num + 1 - val;
            return val;
        }
        ++reroll;
        while (reroll-- > 0) {
            final int val = rand_choice(num) + 1;
            if (val > max) {
                max = val;
            }
        }
        return max;
    }
    
    public static int rand_rollMean(int sides, int reroll, final int scale) {
        if (sides == 0) {
            return 0;
        }
        if (sides < 0) {
            return -rand_rollMean(-sides, reroll, scale);
        }
        if (reroll == 0) {
            int mean = (sides + 1) * scale;
            mean = (mean + 1) / 2;
            return mean;
        }
        if (reroll < 0) {
            int mean = rand_rollMean(sides, -reroll, scale);
            mean = (sides + 1) * scale - mean;
            return mean;
        }
        if (sides < 2) {
            return scale;
        }
        if (sides > rand.DPDF_MAXSIDES) {
            sides = rand.DPDF_MAXSIDES;
        }
        if (reroll > rand.DPDF_MAXROLLS) {
            reroll = rand.DPDF_MAXROLLS;
        }
        int mean = rand.glb_dpdftable[reroll][sides - 2];
        mean *= scale;
        mean += rand.DPDF_SCALE / 2;
        mean /= rand.DPDF_SCALE;
        return mean;
    }
    
    public static int rand_dice(final int num, final int sides, final int bonus) {
        int total = bonus;
        for (int i = 0; i < num; ++i) {
            total += rand_choice(sides) + 1;
        }
        return total;
    }
    
    public static boolean rand_chance(final int percentage) {
        if (percentage == 0) {
            return false;
        }
        if (percentage == 100) {
            return true;
        }
        final int percent = rand_choice(100);
        return percent < percentage;
    }
    
    public static byte rand_sign() {
        return (byte)(rand_choice(2) * 2 - 1);
    }
    
    public static Vector2 rand_direction() {
        return getDirection(rand_choice(4));
    }
    
    public static Vector2 rand_eightwaydirection() {
        byte x = rand_sign();
        byte y = rand_sign();
        if (rand_choice(2) > 0) {
            if (rand_choice(2) > 0) {
                x = 0;
            }
            else {
                y = 0;
            }
        }
        return new Vector2(x, y);
    }
    
    public static int rand_dice(final DICE dice, final int multiplier) {
        return rand_dice(dice.num * multiplier, dice.sides, dice.bonuses * multiplier);
    }
    
    public static int rand_diceMean(final DICE dice, final int multiplier) {
        int total = dice.bonuses * 256 * multiplier;
        for (int i = 0; i < dice.num * multiplier; ++i) {
            total += rand_rollMean(dice.sides, 0, 256);
        }
        total += 128;
        total >>= 8;
        return total;
    }
    
    public static byte[] rand_shuffle(final byte[] set, final int n) {
        for (int i = n - 1; i > 0; --i) {
            final int j = rand_choice(i + 1);
            final byte tmp = set[i];
            set[i] = set[j];
            set[j] = tmp;
        }
        return set;
    }
    
    public static int[] rand_shuffle(final int[] set, final int n) {
        for (int i = n - 1; i > 0; --i) {
            final int j = rand_choice(i + 1);
            final int tmp = set[i];
            set[i] = set[j];
            set[j] = tmp;
        }
        return set;
    }
    
    public static Vector2 getDirection(int dir) {
        dir &= 0x3;
        final byte[] dx = { 0, 1, 0, -1 };
        final byte[] dy = { 1, 0, -1, 0 };
        return new Vector2(dx[dir], dy[dir]);
    }
    
    public static Vector2 rand_angletodir(int angle) {
        angle &= 0x7;
        final byte[] dx = { 1, 1, 0, -1, -1, -1, 0, 1 };
        final byte[] dy = { 0, 1, 1, 1, 0, -1, -1, -1 };
        return new Vector2(dx[angle], dy[angle]);
    }
    
    public static int rand_dirtoangle(final Vector2 vec) {
        for (int a = 0; a < 8; ++a) {
            final Vector2 vec2 = rand_angletodir(a);
            if (vec2.x == vec.x && vec2.y == vec.y) {
                return a;
            }
        }
        return rand_range(0, 7);
    }
    
    public static int rand_wanginthash(int key) {
        key += ~(key << 16);
        key ^= key >>> 5;
        key += key << 3;
        key ^= key >>> 13;
        key += ~(key << 9);
        key ^= key >>> 17;
        return key;
    }
    
    public static int rand_hashstring(final String s) {
        return rand_wanginthash(s.hashCode());
    }
    
    public static String rand_name(final int length) {
        final String vowels = "aaaeeeiiiooouuyy'";
        final String frictive = "rsfhvnmz";
        final String plosive = "tpdgkbc";
        final String weird = "qwjx";
        int syllables = 0;
        int pos = 0;
        boolean prime = false;
        final StringBuilder name = new StringBuilder();
        char state;
        if (rand_chance(30)) {
            state = 'v';
        }
        else if (rand_chance(40)) {
            state = 'f';
        }
        else if (rand_chance(40)) {
            state = 'p';
        }
        else {
            state = 'w';
        }
        while (pos < length - 1) {
            switch (state) {
                case 'v': {
                    name.append(vowels.charAt(rand_choice(vowels.length())));
                    if (!prime) {
                        ++syllables;
                    }
                    ++pos;
                    break;
                }
                case 'f': {
                    name.append(frictive.charAt(rand_choice(frictive.length())));
                    ++pos;
                    break;
                }
                case 'p': {
                    name.append(plosive.charAt(rand_choice(plosive.length())));
                    ++pos;
                    break;
                }
                case 'w': {
                    name.append(weird.charAt(rand_choice(weird.length())));
                    ++pos;
                    break;
                }
            }
            if (syllables >= 3 && pos >= 3 && rand_chance(20 + pos * 4)) {
                break;
            }
            switch (state) {
                case 'v': {
                    if (!prime && rand_chance(10)) {
                        state = 'v';
                        prime = true;
                        continue;
                    }
                    if (rand_chance(40)) {
                        state = 'f';
                    }
                    else if (rand_chance(70)) {
                        state = 'p';
                    }
                    else {
                        state = 'w';
                    }
                    prime = false;
                    continue;
                }
                case 'f': {
                    if (!prime && rand_chance(50)) {
                        prime = true;
                        state = 'p';
                        continue;
                    }
                    state = 'v';
                    prime = false;
                    continue;
                }
                case 'p': {
                    if (!prime && rand_chance(10)) {
                        prime = true;
                        state = 'f';
                        continue;
                    }
                    state = 'v';
                    prime = false;
                    continue;
                }
                case 'w': {
                    state = 'v';
                    prime = false;
                }
                default: {
                    continue;
                }
            }
        }
        final String s = name.toString();
        return String.valueOf(s.substring(0, 1).toUpperCase()) + s.substring(1);
    }
}

class DICE
{
    int num;
    int sides;
    int bonuses;
}
