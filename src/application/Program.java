package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Department obj = new Department(1,"Books");
		//Seller seller = new Seller(21,"Bob","bob@gmail.com",new Date(),3000.0,obj);
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== Teste 1 findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("=== Teste 2 findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller sl: list) {
			System.out.println(sl);
		}
		
		System.out.println("=== Teste 3 findByAll ===");
		list = sellerDao.findAll();
		for(Seller sl: list) {
			System.out.println(sl);
		}
		
		System.out.println("=== Teste 4 seller insert ===");
		Seller newSeller = new Seller(null,"Greg","greg@gmail.com",new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted new Seller "+ newSeller.getId());
		
		System.out.println("=== Teste 5 seller update ===");
		seller = sellerDao.findById(1);
		seller.setName("Martha Waine");
		sellerDao.update(seller);
		System.out.println("Update Complete");
	}

}
