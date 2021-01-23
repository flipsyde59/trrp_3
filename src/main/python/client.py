#!/usr/bin/env python

import urllib.request
import customer_pb2

Customer = customer_pb2.Customer

def requests():
    customer = Customer()
    customers_read = urllib.request.urlopen('http://localhost:8080/customers/1').read()
    customer.ParseFromString(customers_read)
    print(customer)


if __name__ == '__main__':
    file = open('clients.txt')
    customers = []
    for line in file.read().split('\n'):
        customers.append(line.split(', '))
        file.close()
    #requests()
