package com.biz.bank.exec;

import com.biz.bank.service.BankService;

public class BankExec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String strbalance = "src/com/biz/bank/bankBalance.txt";
		
		
		BankService bs = new BankService(strbalance);
		
		bs.readBalance();
		bs.bankMenu();

	}

}
