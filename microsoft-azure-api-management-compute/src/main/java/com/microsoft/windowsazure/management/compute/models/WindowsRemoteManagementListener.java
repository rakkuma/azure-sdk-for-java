// 
// Copyright (c) Microsoft and contributors.  All rights reserved.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// 
// See the License for the specific language governing permissions and
// limitations under the License.
// 

// Warning: This code was generated by a tool.
// 
// Changes to this file may cause incorrect behavior and will be lost if the
// code is regenerated.

package com.microsoft.windowsazure.management.compute.models;

import com.microsoft.windowsazure.management.compute.models.VirtualMachineWindowsRemoteManagementListenerType;

/**
* Contains the type and certificate information for the listener.
*/
public class WindowsRemoteManagementListener
{
    private String certificateThumbprint;
    
    /**
    * Optional. Specifies the certificate thumbprint for the secure connection.
    * If Type is Https then this value is an optional value that is set to the
    * thumbprint of the service certificate that is used to provision the
    * WinRM HTTPS listener. If this value is not specified, a self-signed
    * certificate is generated and used for the virtual machine.
    */
    public String getCertificateThumbprint() { return this.certificateThumbprint; }
    
    /**
    * Optional. Specifies the certificate thumbprint for the secure connection.
    * If Type is Https then this value is an optional value that is set to the
    * thumbprint of the service certificate that is used to provision the
    * WinRM HTTPS listener. If this value is not specified, a self-signed
    * certificate is generated and used for the virtual machine.
    */
    public void setCertificateThumbprint(String certificateThumbprint) { this.certificateThumbprint = certificateThumbprint; }
    
    private VirtualMachineWindowsRemoteManagementListenerType listenerType;
    
    /**
    * Specifies the type of listener. This value can be Http or Https. The
    * value is case sensitive.
    */
    public VirtualMachineWindowsRemoteManagementListenerType getListenerType() { return this.listenerType; }
    
    /**
    * Specifies the type of listener. This value can be Http or Https. The
    * value is case sensitive.
    */
    public void setListenerType(VirtualMachineWindowsRemoteManagementListenerType listenerType) { this.listenerType = listenerType; }
    
    /**
    * Initializes a new instance of the WindowsRemoteManagementListener class.
    *
    */
    public WindowsRemoteManagementListener()
    {
    }
}
