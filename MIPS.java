import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 *  * @author Sunny  *   
 */
public class MIPS {

	static String rs = null;
	static String rt = null;
	static String rd = null;
	static String shamt = null;
	static String funct = null;
	static String imm = null;

	public static final int NO_OF_REGISTERS = 32;
	private static int[] registerArray = new int[NO_OF_REGISTERS];

	/**
	 * This is the class that handles instructions such as lw, sw, sb, lb, and
	 * lbu. It's titled Memory because the above mentioned instructions use
	 * memory.  
	 */
	public static class Memory {
		private static Memory vMem;
		private int[] memory;

		private Memory() {
			memory = new int[(int) Math.pow(2, 21)];
		}

		public static void initialize() {
			vMem = new Memory();
		}

		public static Memory getInstance() {
			if (vMem == null) {
				vMem = new Memory();
			}
			return vMem;
		}

		public int getMemoryValue(int address) {
			return memory[address];
		}

		public void setMemoryValue(int address, int value) {
			memory[address] = value;
		}
	}

	public static String addLeadingZeros(String s) {
		String temp = "";
		int length = 32 - s.length();
		for (int i = 0; i < length; i++) {
			temp += "0";
		}
		temp += s;
		return temp;
	}

	public static String addLeadingZerosHex(String s) {
		String temp = "";
		int length = 8 - s.length();
		for (int i = 0; i < length; i++) {
			temp += "0";
		}
		temp += s;
		return temp;
	}

	public static String hexToBinary(String hex) {
		Long decimal = Long.parseLong(hex, 16);
		String binary = Long.toBinaryString(decimal);
		return binary;
	}

	public static void main(String[] args) {

		for (int i = 0; i < 32; i++) {
			registerArray[i] = 0;
		}
		Memory.initialize(); //initialize memory

		Scanner sc = new Scanner(System.in);
		String binary = null, binaryLZ = null;
		String opcode;
		int zeroCount = 0, oneCount = 0;
		String strLine = "";
		System.out.printf("Please enter the number of lines... ");
		System.out.println();
		int counter = 0;
		int i = 0;
		counter = sc.nextInt();
		String[] hexLine = new String[counter+1];
		System.out.println("Please enter the hexadecimal machine code... ");

		while (i <= counter) {
			strLine = sc.nextLine();
			hexLine[i] = strLine;
			i++;
		}
		for (i = 0; i <= counter; i++) {
			strLine = hexLine[i];
			if (strLine.equals("0")) {
				zeroCount++;
			} else if (strLine.equals("1")) {
				oneCount++;
			} else if (strLine.length() == 8) {
				binary = hexToBinary(strLine);

				if (binary.length() != 32)
					binaryLZ = addLeadingZeros(binary);
				else
					binaryLZ = binary;

				opcode = binaryLZ.substring(0, 6);

				/**
				 * Now the fun begins. If opcode == "000000" then instruction is
				 * R-type. Else it's I type. (Not implementing I-type).
				 */
				if (opcode.equals("000000")) {
					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					rd = binaryLZ.substring(16, 21);
					shamt = binaryLZ.substring(21, 26);
					funct = binaryLZ.substring(26, 32);

					if (funct.equals("100000")) { // Add
						int rsi = Integer.parseInt(rs, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = registerArray[rsi]
								+ registerArray[rti];

					} else if (funct.equals("100001")) { // AddU
						int rsi = Integer.parseInt(rs, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = registerArray[rsi]
								+ registerArray[rti];

					} else if (funct.equals("100100")) { // and
						int rsi = Integer.parseInt(rs, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = registerArray[rti]
								& registerArray[rsi];

					} else if (funct.equals("100111")) { // nor
						int rsi = Integer.parseInt(rs, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = ~(registerArray[rsi] | registerArray[rti]);

					} else if (funct.equals("101010")) { // slt
						int rsi = Integer.parseInt(rs, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = (registerArray[rsi] < registerArray[rti]) ? 1
								: 0;

					} else if (funct.equals("101011")) { // sltu
						int rsi = Integer.parseInt(rs, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = (registerArray[rsi] < registerArray[rti]) ? 1
								: 0;

					} else if (funct.equals("000000")) { // sll
						int shamti = Integer.parseInt(shamt, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = registerArray[rti] << shamti;

					} else if (funct.equals("000010")) { // slr
						int shamti = Integer.parseInt(shamt, 2);
						int rti = Integer.parseInt(rt, 2);
						int rdi = Integer.parseInt(rd, 2);

						registerArray[rdi] = registerArray[rti] >> shamti;
					}

				} else if (opcode.equals("001111")) { // lui
					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int immi = Integer.parseInt(imm, 2); // 8
					int rti = Integer.parseInt(rt, 2); // 0

					registerArray[rti] = immi << 16;

				} else if (opcode.equals("001010")) { // slti
					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					registerArray[rti] = (registerArray[rsi] < immi) ? 1 : 0;

				} else if (opcode.equals("001001")) { // addiu

					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					registerArray[rti] = registerArray[rsi] + immi;

				} else if (opcode.equals("100100")) { // lbu
					Memory memory = Memory.getInstance();

					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					registerArray[rti] = memory
							.getMemoryValue(registerArray[rsi] + immi);

				} else if (opcode.equals("100011")) { // lw
					Memory memory = Memory.getInstance();

					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					registerArray[rti] = memory
							.getMemoryValue(registerArray[rsi] + immi);

				} else if (opcode.equals("101000")) { // sb
					Memory memory = Memory.getInstance();

					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					memory.setMemoryValue(registerArray[rsi] + immi,
							registerArray[rti] & 0xFF);

				} else if (opcode.equals("101011")) { // sw
					Memory memory = Memory.getInstance();

					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					memory.setMemoryValue(registerArray[rsi] + immi,
							registerArray[rti]);

				} else if (opcode.equals("100000")) { // lb
					Memory memory = Memory.getInstance();

					rs = binaryLZ.substring(6, 11);
					rt = binaryLZ.substring(11, 16);
					imm = binaryLZ.substring(16, 32);

					int rsi = Integer.parseInt(rs, 2);
					int rti = Integer.parseInt(rt, 2);
					int immi = Integer.parseInt(imm, 2);

					registerArray[rti] = memory
							.getMemoryValue(registerArray[rsi] + immi);
				}

				/** Now we print */
				if (oneCount == 1) {
					oneCount++;
				}
				if (oneCount == 2) {
					for (int j = 0; j < 32; j++) {
						if ((j != 0) && (j % 4 == 0)) {
							System.out.println();
						}
						String thr = addLeadingZeros(Integer
								.toBinaryString(registerArray[j]));
						String ltr = Long.toHexString(Long.parseLong(thr, 2));

						if ((j + 1) % 4 == 0) {
							System.out.printf("$%d:0x%s", j,
									addLeadingZerosHex(ltr));
						} else if ((j + 1) % 4 != 0) {
							System.out.printf("$%d:0x%s  ", j,
									addLeadingZerosHex(ltr));
						}

						if (j == 31) {
							System.out.println();
							System.out
							.println("========================================");
						}
					}
				}

			} else {
				//System.out.printf("The following line caused the error: \"%s\". Input must be in Hexadecimal.\n",
				//hexLine[i]);
			}
		}
		if (zeroCount == 1) {
			for (int j = 0; j < 32; j++) {
				if ((j != 0) && (j % 4 == 0)) {
					System.out.println();
				}
				String thr = addLeadingZeros(Integer
						.toBinaryString(registerArray[j]));
				String ltr = Long.toHexString(Long.parseLong(thr, 2));

				if ((j + 1) % 4 == 0) {
					System.out.printf("$%d:0x%s", j, addLeadingZerosHex(ltr));
				} else if ((j + 1) % 4 != 0) {
					System.out.printf("$%d:0x%s  ", j, addLeadingZerosHex(ltr));
				} else {
					;
				}

				if (j == 31) {
					System.out.println();
					System.out
					.println("========================================");
				}
			}
		}
	}
}
