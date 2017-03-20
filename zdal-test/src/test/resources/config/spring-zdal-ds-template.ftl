<?xml version="1.0" encoding="GBK"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd"
	default-autowire="byName">
	
	<bean id="${appName}" class="com.alipay.zdal.client.config.bean.ZdalAppBean">
		<property name="appName" value="${appName}" />
		<property name="dbmode" value="${dbmode}" />
		<property name="idcName" value="${idcName}" />
		<#if appDataSourceList?? >
		<property name="appDataSourceList">
			<list>		
				<#list appDataSourceList as ds>
				<ref bean="${ds.appDsName}" />
				</#list>
			</list>
		</property>
		</#if>
	</bean>
	
	<#if appDataSourceList?? >
		<#list appDataSourceList as appDataSource>
	<bean id="${appDataSource.appDsName}" class="com.alipay.zdal.client.config.bean.AppDataSourceBean">
		<property name="appDataSourceName" value="${appDataSource.appDsName}" />
		<property name="dataBaseType" value="${appDataSource.dbType}" />
		<property name="configType" value="${appDataSource.dataSourceConfigType}" />
		<property name="zoneError" value="${appDataSource.zoneError}" />
		<#if appDataSource.shardTableRules?size gt 0 > 
		<property name="appRule" ref="${appDataSource.appDsName}Rule" />
		</#if>
		<property name="zoneDSSet" >
			<set>
			<#if appDataSource.zoneDs?? >
				<#list appDataSource.zoneDs as zone>
				<value>${zone}</value>
				</#list>
			</#if>
			</set>
		</property>
		
		<property name="physicalDataSourceSet">
			<set>
				<#list appDataSource.dataSourceParameters?keys as key >
				<ref bean="${appDataSource.appDsName}${key}"/>
				</#list>
			</set>
		</property>
		<#if appDataSource.readWriteRules?? >
		<property name="groupDataSourceRuleMap">
			<map>
				<#list appDataSource.readWriteRules?keys as key >
				<entry key="${key}" value="${appDataSource.readWriteRules[key]}" />
				</#list>
			</map>
		</property>
		</#if>
	</bean>
		</#list>
	</#if>
	
	<#list appDataSourceList as appDataSource >
	<#list appDataSource.dataSourceParameters?keys as key >
		<#assign dataSource = appDataSource.dataSourceParameters[key] > 

	<bean id="${appDataSource.appDsName}${key}" class="com.alipay.zdal.client.config.bean.PhysicalDataSourceBean" >
		<property name="name" value="${key}" />
		<property name="jdbcUrl" value="${dataSource.jdbcUrl}" />
	    <property name="userName" value="${dataSource.userName}" />
	    <property name="password" value="${dataSource.password}" />
	    <property name="minConn" value="${dataSource.minConn?c}" />
	    <property name="maxConn" value="${dataSource.maxConn?c}" />
	    <property name="blockingTimeoutMillis" value="${dataSource.blockingTimeoutMillis?c}" />
	    <property name="idleTimeoutMinutes" value="${dataSource.idleTimeoutMinutes?c}" />
	    <property name="preparedStatementCacheSize" value="${dataSource.preparedStatementCacheSize?c}" />
	    <property name="queryTimeout" value="${dataSource.queryTimeout?c}" />
	    <#if dataSource.prefill == true >
	    <property name="prefill" value="true" />
	    <#else>
	    <property name="prefill" value="false" />
	    </#if>
	    <property name="maxReadThreshold" value="100" />
	    <property name="maxWriteThreshold" value="100" />
	<#if failoverLogicPhysicsDsNameMap[key]?? >
		<property name="failoverRule" value="failover" />
	<#else>
		<property name="failoverRule" value="master" />
	</#if>
		<property name="logicDbNameSet">
			<set>
			<#if failoverLogicPhysicsDsNameMap[key]?? >
				<#if failoverLogicPhysicsDsNameMap[key]?? >
				<#list failoverLogicPhysicsDsNameMap[key] as failOverLogDsName>
				<value>${failOverLogDsName}</value>
				</#list>
				</#if>
			<#else>
				<#if masterLogicPhysicsDsNameMap[key]?? >
				<#list masterLogicPhysicsDsNameMap[key] as logDsName>
				<value>${logDsName}</value>
				</#list>
				</#if>
			</#if>
			</set>
		</property>
	</bean>
	</#list>
	
	</#list>
	
</beans>