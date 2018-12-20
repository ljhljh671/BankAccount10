package com.biz.bank.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.biz.bank.vo.BankVO;

public class BankService {

	List<BankVO> bankList;
	String balFile;
	Scanner scan = new Scanner(System.in);
	String strFolder = "src/com/biz/bank/iolist/";

	public BankService(String balFile) {
		bankList = new ArrayList();
		this.balFile = balFile;

	}

	public void readBalance() {
		FileReader fr;
		BufferedReader buffer;

		try {

			fr = new FileReader(balFile);
			buffer = new BufferedReader(fr);

			while (true) {

				String strread = buffer.readLine();
				if (strread == null)
					break;
				String[] splword = strread.split(":");
				BankVO vo = new BankVO();

				vo.setStrID(splword[0]);
				vo.setIntBalance(Integer.valueOf(splword[1]));
				vo.setStrLastDate(splword[2]);

				bankList.add(vo);

			}
			buffer.close();
			fr.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void bankMenu() {

		while (true) {

			System.out.println("번호를 눌러주세요");
			System.out.println("1. 입금");
			System.out.println("2. 출금");
			System.out.println("3. 계좌조회");
			System.out.println("0. 종료");

			System.out.print("번호 : ");
			String strScanner = scan.nextLine();

			int intM = Integer.valueOf(strScanner);
			if (intM == 0)
				System.out.println("감사합니다");
			if (intM == 1)
				this.bankInput();
//				System.out.println("입금");
			if (intM == 2)
				this.bankOutput();
//				System.out.println("출금");
			if (intM == 3)
				System.out.println("계좌조회");

		}
	}

	public void bankInput() {

		System.out.print("계좌번호 :");

		String strId = scan.nextLine();
		BankVO vo = bankFindId(strId);
		if (vo == null)
			return;

		// 계좌번호가 정상이고, vo에는
		// 해당 계좌번호의 정보가 담겨 있다.

		System.out.println("입금액 >> ");
		String strIO = scan.nextLine();
		int intIO = Integer.valueOf(strIO);

		// 새로운 코드
		// 입금일 경우 vo.strIO 에 "입금" 문자열 저장
		// vo.intIOCash에 입금금액을 저장
		// vo.balance에 +입금액을 저장한다.

		vo.setStrIO("입금");
		vo.setIntInCash(intIO);
		vo.setIntBalance(vo.getIntBalance() + intIO);

		// old java 코드로 날짜 설정하기
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		String strDate = sm.format(curDate);

		// new java(1.8) 코드로 현재 날짜 가져오기
		LocalDate ld = LocalDate.now();
		strDate = ld.toString();

		vo.setStrLastDate(strDate);
		bankIOWrite(vo);
		System.out.println("입금처리 완료!!!");

	}

	public BankVO bankFindId(String strId) {
		/*
		 * 계좌번호를 매개변수로 받아서 bankList에서 계좌를 조회하고 bankList에 계좌가 있으면 찾은 Bank(vo)를 return 하고
		 * 없으면 null을 return 하도록 한다.
		 */

		for (BankVO vo : bankList) {

			if (strId.equals(vo.getStrID())) {
				return vo;
			}
		}
		return null;

	}

	public void bankOutput() {

		System.out.print("계좌번호 :");

		String strId = scan.nextLine();
		BankVO vo = bankFindId(strId);
		if (vo == null) {
			System.out.println("계좌번호 오류");
			return;
		}

		System.out.println("출금액 >> ");
		String strIO = scan.nextLine();
		int intIO = Integer.valueOf(strIO);

		if (intIO > vo.getIntBalance()) {
			System.out.println("잔액이 부족합니다");
			return;

		}

		vo.setStrIO("출금");
		vo.setIntInCash(intIO);
		vo.setIntBalance(vo.getIntBalance() - intIO);
		bankIOWrite(vo);

		System.out.println("출금처리 완료!!!");

	}

	public void bankIOWrite(BankVO vo) {

		FileWriter fw;
		PrintWriter pw;
		String strId = vo.getStrID();

		try {
			fw = new FileWriter(strFolder + strId, true);
			pw = new PrintWriter(fw);

			if (vo.getStrIO().equals("입금")) {
				pw.printf("%s:%s:%s:%d:%d:%d", vo.getStrID(), vo.getStrLastDate(), vo.getStrIO(), vo.getIntInCash(), 0,
						vo.getIntBalance());
				pw.println();
			} else if (vo.getStrIO().equals("출금")) {
				pw.printf("%s:%s:%s:%d:%d:%d", vo.getStrID(), vo.getStrLastDate(), vo.getStrIO(), 0, vo.getIntInCash(),
						vo.getIntBalance());
				pw.println();
			}
			pw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
