package com.github.esrrhs.texas_algorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class GenUtil
{
	public static long totalKey = 0;
	public static FileOutputStream out;
	public static int lastPrint = 0;
	public static long beginPrint;
	public static final long genNum = 52;
	public static final long total = (genNum * (genNum - 1) * (genNum - 2) * (genNum - 3) * (genNum - 4) * (genNum - 5)
			* (genNum - 6)) / (7 * 6 * 5 * 4 * 3 * 2);
	public static ArrayList<Long> keys = new ArrayList<>((int) total);

	public static void genKey()
	{
		try
		{
			beginPrint = System.currentTimeMillis();

			genCard();

			System.out.println("genKey finish " + total);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void genCard() throws Exception
	{
		ArrayList<Integer> list = new ArrayList<>();
		for (byte i = 0; i < 4; ++i)
		{
			for (byte j = 0; j < genNum / 4; ++j)
			{
				list.add((Integer) (int) (new Poke(i, (byte) (j + 2))).toByte());
			}
		}
		Collections.sort(list);

		int[] tmp = new int[7];
		permutation(list, 0, 0, 7, tmp);
	}

	public static void permutation(ArrayList<Integer> a, int count, int count2, int except, int[] tmp) throws Exception
	{
		if (count2 == except)
		{
			genCardSave(tmp);
		}
		else
		{
			for (int i = count; i < a.size(); i++)
			{
				tmp[count2] = a.get(i);
				permutation(a, i + 1, count2 + 1, except, tmp);
			}
		}
	}

	private static void genCardSave(int[] tmp) throws Exception
	{
		final long c = genCardBind(tmp);

		keys.add(c);
		totalKey++;

		int cur = (int) (totalKey * 100 / total);
		if (cur != lastPrint)
		{
			lastPrint = cur;

			long now = System.currentTimeMillis();
			float per = (float) (now - beginPrint) / totalKey;
			System.out.println(
					cur + "% 需要" + per * (total - totalKey) / 60 / 1000 + "分" + " 用时" + (now - beginPrint) / 60 / 1000
							+ "分" + " 速度" + totalKey / ((float) (now - beginPrint) / 1000) + "条/秒");
		}
	}

	private static long genCardBind(int[] tmp)
	{
		long ret = 0;
		for (Integer i : tmp)
		{
			ret = ret * 100 + i;
		}
		return ret;
	}

	public static void outputData()
	{
		try
		{
			totalKey = 0;
			lastPrint = 0;
			beginPrint = System.currentTimeMillis();

			File file = new File("texas_data.txt");
			if (file.exists())
			{
				file.delete();
			}
			file.createNewFile();
			out = new FileOutputStream(file, true);

			for (int i = 0; i < keys.size() - 1; i++)
			{
				int min = i;
				for (int j = i + 1; j < keys.size(); j++)
				{
					if (compare(keys.get(j), keys.get(min)))
					{
						min = j;
					}
				}

				if (min != i)
				{
					long tmp = keys.get(min);
					keys.set(min, keys.get(i));
					keys.set(i, tmp);
				}

				String str = keys.get(i) + " " + i + " " + keys.size() + " " + toString(keys.get(i)) + " "
						+ max(keys.get(i)) + " " + toString(max(keys.get(i))) + " " + maxType(keys.get(i)) + "\n";

				out.write(str.getBytes("utf-8"));

				totalKey++;
				int cur = (int) (totalKey * 100 / total);
				if (cur != lastPrint)
				{
					lastPrint = cur;

					long now = System.currentTimeMillis();
					float per = (float) (now - beginPrint) / totalKey;
					System.out.println(cur + "% 需要" + per * (total - totalKey) / 60 / 1000 + "分" + " 用时"
							+ (now - beginPrint) / 60 / 1000 + "分" + " 速度"
							+ totalKey / ((float) (now - beginPrint) / 1000) + "条/秒");
				}
			}

			out.close();

			System.out.println("outputData finish " + total);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static long max(long k)
	{
		ArrayList<Poke> cs = new ArrayList<>();
		cs.add(new Poke((byte) (k % 100000000000000L / 1000000000000L)));
		cs.add(new Poke((byte) (k % 1000000000000L / 10000000000L)));
		cs.add(new Poke((byte) (k % 10000000000L / 100000000L)));
		cs.add(new Poke((byte) (k % 100000000L / 1000000L)));
		cs.add(new Poke((byte) (k % 1000000L / 10000L)));
		cs.add(new Poke((byte) (k % 10000L / 100L)));
		cs.add(new Poke((byte) (k % 100L / 1L)));
		ArrayList<Poke> pickedCards1 = new ArrayList<>();
		TexasCardUtil.fiveFromSeven(cs, pickedCards1);

		long ret = 0;
		for (Poke p : pickedCards1)
		{
			ret = ret * 100 + p.toByte();
		}
		return ret;
	}

	public static int maxType(long k)
	{
		ArrayList<Poke> cs = new ArrayList<>();
		cs.add(new Poke((byte) (k % 100000000000000L / 1000000000000L)));
		cs.add(new Poke((byte) (k % 1000000000000L / 10000000000L)));
		cs.add(new Poke((byte) (k % 10000000000L / 100000000L)));
		cs.add(new Poke((byte) (k % 100000000L / 1000000L)));
		cs.add(new Poke((byte) (k % 1000000L / 10000L)));
		cs.add(new Poke((byte) (k % 10000L / 100L)));
		cs.add(new Poke((byte) (k % 100L / 1L)));
		ArrayList<Poke> pickedCards1 = new ArrayList<>();
		TexasCardUtil.fiveFromSeven(cs, pickedCards1);

		return TexasCardUtil.getCardTypeUnordered(pickedCards1);
	}

	public static String toString(long k)
	{
		ArrayList<Poke> cs = new ArrayList<>();
		if (k > 1000000000000L)
		{
			cs.add(new Poke((byte) (k % 100000000000000L / 1000000000000L)));
		}
		if (k > 10000000000L)
		{
			cs.add(new Poke((byte) (k % 1000000000000L / 10000000000L)));
		}
		cs.add(new Poke((byte) (k % 10000000000L / 100000000L)));
		cs.add(new Poke((byte) (k % 100000000L / 1000000L)));
		cs.add(new Poke((byte) (k % 1000000L / 10000L)));
		cs.add(new Poke((byte) (k % 10000L / 100L)));
		cs.add(new Poke((byte) (k % 100L / 1L)));
		String ret = "";
		for (Poke poke : cs)
		{
			ret += poke;
		}
		return ret;
	}

	public static boolean compare(long k1, long k2)
	{
		ArrayList<Poke> cs1 = new ArrayList<>();
		cs1.add(new Poke((byte) (k1 % 100000000000000L / 1000000000000L)));
		cs1.add(new Poke((byte) (k1 % 1000000000000L / 10000000000L)));
		cs1.add(new Poke((byte) (k1 % 10000000000L / 100000000L)));
		cs1.add(new Poke((byte) (k1 % 100000000L / 1000000L)));
		cs1.add(new Poke((byte) (k1 % 1000000L / 10000L)));
		cs1.add(new Poke((byte) (k1 % 10000L / 100L)));
		cs1.add(new Poke((byte) (k1 % 100L / 1L)));
		ArrayList<Poke> pickedCards1 = new ArrayList<>();
		TexasCardUtil.fiveFromSeven(cs1, pickedCards1);

		ArrayList<Poke> cs2 = new ArrayList<>();
		cs2.add(new Poke((byte) (k2 % 100000000000000L / 1000000000000L)));
		cs2.add(new Poke((byte) (k2 % 1000000000000L / 10000000000L)));
		cs2.add(new Poke((byte) (k2 % 10000000000L / 100000000L)));
		cs2.add(new Poke((byte) (k2 % 100000000L / 1000000L)));
		cs2.add(new Poke((byte) (k2 % 1000000L / 10000L)));
		cs2.add(new Poke((byte) (k2 % 10000L / 100L)));
		cs2.add(new Poke((byte) (k2 % 100L / 1L)));
		ArrayList<Poke> pickedCards2 = new ArrayList<>();
		TexasCardUtil.fiveFromSeven(cs2, pickedCards2);

		return TexasCardUtil.compareCards(pickedCards1, pickedCards2) < 0;
	}
}