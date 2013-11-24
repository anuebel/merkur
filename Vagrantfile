Vagrant.configure("2") do |config|
  config.vm.box = "precise32"
  
  config.vm.define "merkur-server-vm" do |msvm|
    msvm.vm.hostname = "merkur-server-vm"
    msvm.vm.network "private_network", ip: "192.168.50.190"
    msvm.vm.network :forwarded_port, host:7777, guest:80
    msvm.vm.network :forwarded_port, host:8888, guest:15672
    msvm.vm.provision :shell, :path => "bootstrap-server.sh"
  end

  config.vm.define "merkur-client-1-vm" do |mcvm1|
    mcvm1.vm.hostname = "merkur-client-1-vm"
    mcvm1.vm.network "private_network", ip: "192.168.50.191"
    mcvm1.vm.provision :shell, :path => "bootstrap-client.sh"
  end
  
end
