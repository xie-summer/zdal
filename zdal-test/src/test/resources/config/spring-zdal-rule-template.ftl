<?xml version="1.0" encoding="GBK"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd"
	default-autowire="byName">
	<#list appDsNameList as appDsName>
	<bean id="${appDsName}Rule" class="com.alipay.zdal.rule.config.beans.AppRule"
		init-method="init">
		<#if masterRuleMap[appDsName]?? >
		<property name="masterRule" ref="${appDsName}MasterRule" />
		</#if>
		<#if slaveRuleMap[appDsName]?? >
		<property name="slaveRule" ref="${appDsName}SlaveRule" />
		</#if>
		<#if readwriteRuleMap[appDsName]?? >
		<property name="readwriteRule" ref="${appDsName}ReadWriteRule" />
		</#if>
	</bean>
	
	<#if masterRuleMap[appDsName]?? >
	<bean id="${appDsName}MasterRule" class="com.alipay.zdal.rule.config.beans.ShardRule">
		<#if dbTypeMap[appDsName]?? >
		<property name="dbtype" value="${dbTypeMap[appDsName]}" />
		</#if>
		<property name="tableRules">
			<map>
				<#list masterRuleMap[appDsName] as shardRule>
				<entry key="${shardRule.logicTableName}" value-ref="${appDsName}_${shardRule.logicTableName}" />
				</#list>
			</map>
		</property>
		<property name="defaultDbIndex" value="" />
	</bean>
	</#if>
	
	<#if slaveRuleMap[appDsName]?? >
	<bean id="${appDsName}SlaveRule" class="com.alipay.zdal.rule.config.beans.ShardRule">
		<#if dbTypeMap[appDsName]?? >
		<property name="dbtype" value="${dbTypeMap[appDsName]}" />
		</#if>
		<property name="tableRules">
			<map>
				<#list slaveRuleMap[appDsName] as shardRule>
				<entry key="${shardRule.logicTableName}" value-ref="${appDsName}_${shardRule.logicTableName}" />
				</#list>
			</map>
		</property>
		<property name="defaultDbIndex" value="" />
	</bean>
	</#if>
	<#if readwriteRuleMap[appDsName]?? >
	<bean id="${appDsName}ReadWriteRule" class="com.alipay.zdal.rule.config.beans.ShardRule">
		<#if dbTypeMap[appDsName]?? >
		<property name="dbtype" value="${dbTypeMap[appDsName]}" />
		</#if>
		<property name="tableRules">
			<map>
				<#list readwriteRuleMap[appDsName] as shardRule>
				<entry key="${shardRule.logicTableName}" value-ref="${appDsName}_${shardRule.logicTableName}" />
				</#list>
			</map>
		</property>
		<property name="defaultDbIndex" value="" />
	</bean>
	</#if>
	
	<#if allRuleMap[appDsName]?? >
	<#list allRuleMap[appDsName] as shardRule>
	<bean id="${appDsName}_${shardRule.logicTableName}" class="com.alipay.zdal.rule.config.beans.TableRule"
		init-method="init">
		<property name="logicTableName" value="${shardRule.logicTableName}" />
		<#if tbNumForEachDbMap[shardRule.logicTableName]?? >
		<property name="tbNumForEachDb" value="${tbNumForEachDbMap[shardRule.logicTableName]}" />
		</#if>
		<#if shardRule.tableSuffix?? >
		<property name="tbSuffix">
			<value>${shardRule.tableSuffix}</value>
		</property>
		</#if>
		<property name="dbIndexes">
			<value>
				${shardRule.dbIndex}
			</value>
		</property>
		<#if shardRule.tableRules?size gt 0 >
		<property name="tbRuleArray">
			<list>
			<#list shardRule.tableRules as tableRule>
			<value>
				${tableRule}
			</value>
			</#list>
			</list>
		</property>
		</#if>
		<#if shardRule.dbRules?size gt 0 >
		<property name="dbRuleList">
			<list>
			<#list shardRule.dbRules as dbRule>
			<value>
				${dbRule}
			</value>
			</#list>
			</list>
		</property>
		</#if>
		<#if tbSuffixPaddingMap[shardRule.logicTableName]?? >
		<property name="tbSuffixPadding">
			<value>
				${tbSuffixPaddingMap[shardRule.logicTableName]}
			</value>
		</property>
		</#if>
		<#if shardRuleMap[shardRule.logicTableName]?? >
		<property name="shardingRules">
			<list>
				<#list shardRuleMap[shardRule.logicTableName] as shardRule>
				<value>
					${shardRule}
				</value>
				</#list>
			</list>
		</property>
		</#if>
	</bean>
	</#list>
	</#if>
	</#list>
</beans>