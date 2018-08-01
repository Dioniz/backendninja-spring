package com.udemy.backendninja.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.udemy.backendninja.component.ContactConverter;
import com.udemy.backendninja.entity.Contact;
import com.udemy.backendninja.model.ContactModel;
import com.udemy.backendninja.repository.ContactRepository;
import com.udemy.backendninja.service.ContactService;

@Service("contactServiceImpl")
public class ContactServiceImpl implements ContactService {

	@Autowired
	@Qualifier("contactRepository")
	ContactRepository contactRepository;

	@Autowired
	@Qualifier("contactConverter")
	ContactConverter contactConverter;
	
	@Override
	public ContactModel addContact(ContactModel contactModel) {
		Contact contact = contactRepository.save(contactConverter.convertContactModel2Contact(contactModel));
		return contactConverter.convertContact2ContactModel(contact);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Override
	public List<ContactModel> listAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		List<ContactModel> contactsModel = new ArrayList<ContactModel>();
		for (Contact contact : contacts) {
			contactsModel.add(contactConverter.convertContact2ContactModel(contact));
		}
		return contactsModel;
	}

	@Override
	public ContactModel findContactById(int id) {
		return contactConverter.convertContact2ContactModel(contactRepository.findById(id));
	}

	@Override
	public void removeContact(int id) {
		ContactModel contactModel = findContactById(id);
		if (null != contactModel) {
			contactRepository.delete(contactConverter.convertContactModel2Contact(contactModel));
		}
	}

}
